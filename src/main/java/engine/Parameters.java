package engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import imgui.type.ImInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Generates and stores all dynamic parameters used by ImGUI
 *
 * The JSON config file currently uses the format
 * category: {
 *     display: "display",
 *     intValue: {
 *         id: "id",
 *         display: "display",
 *         min: min,
 *         max: max,
 *         tags: {
 *             int: true,
 *             validate: true
 *         },
 *     },
 *     listValue: {
 *         id: "id",
 *         display: "display",
 *         list: [
 *             "item1",
 *             "item2"
 *         ],
 *         tags: {
 *             list: true
 *         }
 *     }
 * }
 *
 * There can be any number of categories and any number of any type of values within those categories.
 * The "validate" tag is used to clamp min-max values for integers
 * The "int" and "list" tags are used to check for the data type before casting
 * Floating point values are not currently implemented
 *
 */
public class Parameters {

	public JsonNode config;
	private HashMap<String, Object> flyValues;
	private HashMap<String, ImInt> validation;


	public Parameters(String path) {
		try {
			String s = Files.readString(Path.of(path));
			config = new ObjectMapper().readTree(s);
			flyValues = new HashMap<>();
			validation = new HashMap<>();

			for (Iterator<JsonNode> i1 = config.elements(); i1.hasNext(); ) {
				JsonNode jn1 = i1.next();
				for (Iterator<JsonNode> i2 = jn1.elements(); i2.hasNext(); ) {
					JsonNode jn2 = i2.next();
					if (jn2.has("display")) {
						String jsonPath = jn1.get("display").asText() + "." + jn2.get("display").asText();
						if (jn2.get("tags").has("int")) {
							ImInt i = new ImInt();
							flyValues.put(jsonPath, i);
							flyValues.put(jsonPath + ".max", (jn2.get("max").intValue()));
							flyValues.put(jsonPath + ".min", (jn2.get("min").intValue()));
							validation.put(jsonPath, i);
						}

						if (jn2.get("tags").has("list")) {
							ArrayList<String> a = new ArrayList<>();
							jn2.get("list").elements().forEachRemaining((e) -> a.add(e.asText()));
							flyValues.put(jsonPath, a);
							flyValues.put(jsonPath + ".selection", new ImInt());
							flyValues.put(jsonPath + ".type", "list");
						}

					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clamps values to min/max, currently only int values are allowed
	 */
	public void validate() {
		for (String s : validation.keySet()) {
			ImInt i = validation.get(s);
			Integer min = (Integer) flyValues.get(s + ".min");
			Integer max = (Integer) flyValues.get(s + ".max");
			if (i.get() > max) {
				i.set(max);
			}
			if (i.get() < min) {
				i.set(min);
			}
		}
	}

	public String save(String path) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode o = mapper.createObjectNode();

		for (Iterator<JsonNode> i1 = config.elements(); i1.hasNext(); ) {
			JsonNode jn1 = i1.next();
			for (Iterator<JsonNode> i2 = jn1.elements(); i2.hasNext(); ) {
				JsonNode jn2 = i2.next();
				if (jn2.has("display")) {
					String jsonPath = jn1.get("display").asText() + "." + jn2.get("display").asText();
					String savePath = jn2.get("id").asText();
					if (jn2.get("tags").has("int")) {
						o.put(savePath, ((ImInt) flyValues.get(jsonPath)).get());
					}

					if (jn2.get("tags").has("list")) {
						o.put(savePath, ((ImInt) flyValues.get(jsonPath + ".selection")).get());
					}

				}

			}

		}
		String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o).translateEscapes();
		System.out.println(s);
		return s;
	}

	public Object get(String path) {
		return flyValues.get(path);
	}
}

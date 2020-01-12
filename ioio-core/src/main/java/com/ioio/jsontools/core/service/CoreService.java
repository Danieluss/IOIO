package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.difflib.algorithm.DiffException;
import com.ioio.jsontools.core.rest.data.ModifierData;
import com.ioio.jsontools.core.service.diff.TextDiff;
import com.ioio.jsontools.core.service.filter.JsonFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Provider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/** Service for core functions of jsonTools app.
 *
 * @author Maciej A. Czyzewski
 * @version 1.0
 * @since 2019-11-29
 */
@Service
public class CoreService {
	@Autowired
    private JsonFilter whitelist;
	@Autowired
	private JsonFilter blacklist;
	@Autowired
    private JsonModifier minifier;
	@Autowired
	private JsonModifier maxifier;
	@Autowired
	private Provider<JsonModifier.Builder> jsonModifierBuilderProvider;

	private TextDiff textDiff = new TextDiff();
  
    /** Method used to check if server responses properly. */
  public String ping() {
      return "Server responded properly.";
  }

    /**
     * Method used to whitelist json.
     * @param json to be filtered by whitelist filter
     * @param filter in json format, sets which fields needs to be whitelisted
     */
	public String whitelist(String json, String filter) throws JsonProcessingException {
		return whitelist.filter(json, filter);
	}

    /**
     * Method used to blacklist json.
     * @param json to be filtered by blacklist filter
     * @param filter in json format, sets which fields needs to be blacklisted
     */
	public String blacklist(String json, String filter) throws JsonProcessingException {
		return blacklist.filter(json, filter);
	}

    /**
     * Method used to maxify json - adds spaces and new lines in
     * appropriate places to display json in more human-readable way.
     * @param json to be maxified
     */
	public String maxify(String json) throws JsonProcessingException {
		return maxifier.modify(json);
	}

    /**
     * Method used to minify json - removes spaces and newlines from passed json,
     * to keep the same json structure, but decrease size.
     * @param json to be minified
     */
	public String minify(String json) throws JsonProcessingException {
		return minifier.modify(json);
	}

    /**
     * Method used to combine other filters and modifiers.
     * @param json to be modified by all passed modifiers
     * @param modifiers which needs to be applied on json
     */
	public String combine(String json, List<ModifierData> modifiers) throws JsonProcessingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		JsonModifier.Builder builder = jsonModifierBuilderProvider.get();
        Class<JsonModifier.Builder> builderClass = (Class<JsonModifier.Builder>) builder.getClass();
		for (ModifierData modifier : modifiers) {
			Method method = builderClass.getMethod(modifier.getType().toString().toLowerCase(),String.class);
			method.invoke(builder, modifier.getParams());
        }
		return builder.build().modify(json);
	}

	public String diff(String oldText, String newText) throws DiffException {
		return textDiff.diff(oldText, newText);
	}
}

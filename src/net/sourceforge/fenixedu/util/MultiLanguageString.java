package net.sourceforge.fenixedu.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Language;

public class MultiLanguageString implements Serializable {
    
	private Map<Language, String> contentsMap;

	public MultiLanguageString() {
		this.contentsMap = new HashMap<Language, String>();
	}
    
    public MultiLanguageString(String content) {
        this.contentsMap = new HashMap<Language, String>();
        setContent(content);
    }
	
	public Collection<String> getAllContents() {
		return contentsMap.values();
	}

	public Collection<Language> getAllLanguages() {
		return contentsMap.keySet();
	}


    
    public String getContent() {
        return getContent(getContentLanguage());
    }
	
    public boolean isRequestedLanguage() {
        Language userLanguage = LanguageUtils.getUserLanguage();
        
        if (userLanguage != null && userLanguage.equals(getContentLanguage())) {
            return true;
        }
        
        return false;
    }
    
    public Language getContentLanguage() {
        Language userLanguage = LanguageUtils.getUserLanguage();
        if (userLanguage != null && hasLanguage(userLanguage)) {
            return userLanguage;
        }
        
        Language systemLanguage = LanguageUtils.getSystemLanguage();
        if (systemLanguage != null && hasLanguage(systemLanguage)) {
            return systemLanguage;
        }
        
        return contentsMap.isEmpty() ? null : contentsMap.keySet().iterator().next();
    }
    
    public void setContent(String text) {
        final Language userLanguage = LanguageUtils.getUserLanguage();
        if (userLanguage != null) {
            setContent(userLanguage, text);
        }
        final Language systemLanguage = LanguageUtils.getSystemLanguage();
        if (userLanguage != systemLanguage && !hasLanguage(systemLanguage)) {
            setContent(systemLanguage, text);
        }
    }

	public String getContent(Language language) {
		return contentsMap.get(language);
	}

	public void setContent(Language language, String content) {
		contentsMap.put(language, content);
	}

	public String removeContent(Language language) {
		return contentsMap.remove(language);
	}
	
	public boolean hasLanguage(Language language) {
        return contentsMap.containsKey(language);
	}

	public String exportAsString() {
		final StringBuilder result = new StringBuilder();
		for (final Language key : contentsMap.keySet()) {
            final String value = contentsMap.get(key);
            result.append(key);
            result.append(value.length());
            result.append(':');
            result.append(value);
		}
		return result.toString();
	}
    
    public static MultiLanguageString importFromString(String string) {
        if (string == null) {
            return null;
        }
        
        MultiLanguageString mls = new MultiLanguageString();
        
        for (int i = 0; i < string.length();) {
            String language = string.substring(i, i + 2);
            
            int pos = string.indexOf(':', i + 2);
            int length = Integer.parseInt(string.substring(i + 2, pos));
            
            String content = string.substring(pos + 1, pos + 1 + length);
            mls.setContent(Language.valueOf(language), content);
            
            i = pos + 1 + length;
        }
        
        return mls;
    }
}

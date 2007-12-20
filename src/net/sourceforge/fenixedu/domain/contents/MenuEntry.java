package net.sourceforge.fenixedu.domain.contents;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public interface MenuEntry {

    public String getEntryId();
    public MultiLanguageString getName();
    public MultiLanguageString getTitle();
    public String getPath();
    public boolean isVisible();
    public boolean isAvailable();
    public boolean isAvailable(FunctionalityContext context);
    
    public Collection<MenuEntry> getChildren();
    public Content getReferingContent();
    
}

package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;

public class CreateUnitSiteBanner extends UnitSiteBannerFileService {

    public UnitSiteBanner run(UnitSite site, File mainFile, String mainName, File backFile, String backName, String color, String link, Integer weight) throws FenixServiceException, IOException {
        if (mainFile == null || mainName == null) {
            return null;
        }

        UnitSiteBanner banner = new UnitSiteBanner(site);
        updateBanner(site, banner, mainFile, mainName, backFile, backName, color, link, weight);
        
        return banner;
    }
    
}

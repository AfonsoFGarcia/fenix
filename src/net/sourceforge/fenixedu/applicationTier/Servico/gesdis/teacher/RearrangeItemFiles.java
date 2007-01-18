package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Site;

/**
 * Changes the order of all the given file items to match their position in the
 * list.
 * 
 * @author cfgi
 */
public class RearrangeItemFiles extends Service {

    public void run(Site site, Item holder, List<FileItem> files) {
        holder.setFileItemsOrder(files);
    }

}

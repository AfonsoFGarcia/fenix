
package Dominio;

import java.util.List;

import Util.GuideRequester;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IGuide {
  public Integer getNumber();
  public Integer getYear();
  public Double getTotal();
  public String getRemarks();
  public IPessoa getPerson();
  public IContributor getContributor();
  public List getGuideEntries();
  public GuideRequester getGuideRequester();
  public ICursoExecucao getExecutionDegree();

  public void setNumber(Integer number);
  public void setYear(Integer year);
  public void setTotal(Double total);
  public void setRemarks(String remarks);
  public void setPerson(IPessoa person);
  public void setContributor(IContributor contributor);
  public void setGuideEntries(List guideEntries);
  public void setGuideRequester(GuideRequester guideRequester);
  public void setExecutionDegree(ICursoExecucao executionDegree);
}

/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacy;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * 
 */
public enum Ingression {

    ACO01("Acordo ELF"),

    ACO02("Acordo Angola Telecom"),

    CEA01("Ad-Hoc"),

    CEA02("Cursos M�dios e Superiores"),

    CEA03("Sistemas de Ensino Superior Estrangeiro"),

    CNA01("Contingente Geral"),

    CNA02("Contingente A�ores"),

    CNA03("Contingente Madeira"),

    CNA04("Contingente Macau"),

    CNA05("Contingente Emigrantes"),

    CNA06("Contingente Militar"),

    CNA07("Contingente Deficientes"),

    CON01("Conv�nio Universidade dos A�ores"),

    ENC01("Transfer�ncias Externas"),

    ENC02("Mudan�as de Curso Externas"),

    REA01("Funcion�rios portugueses de miss�o diplom�tica no estrangeiro e seus familiares que os acompanham"),

    REA02(
	    "Cidad�os portugueses bolseiros no estrangeiro, funcion�rios p�blicos em miss�o oficial no estrangeiro ou funcion�rios portugueses da CE e seus familiares que os acompanham"),

    REA03(
	    "Oficiais do quadro permanente das For�as Armadas Portuguesas, no �mbito da satisfa��o de necessidades espec�ficas de forma��o das For�as Armadas."),

    REA04(
	    "Funcion�rios estrangeiros de miss�o diplom�tica acreditada em Portugal e seus familiares aqui residentes em regime de reciprocidade"),

    REA05(
	    "Estudantes nacionais dos pa�ses africanos de express�o portuguesa, bolseiros do Governo Portugu�s, da Funda��o Calouste Gulbenkian e ao abrigo de conven��es com a CE, com frequ�ncia de ensino superior"),

    REA06(
	    "Estudantes nacionais dos pa�ses africanos de express�o portuguesa, bolseiros do Governo Portugu�s, da Funda��o Calouste Gulbenkian e ao abrigo de conven��es com a CE, com o 12� ano de escolaridade portugu�s"),

    REA07("Atletas de Alta Competi��o"),

    REA08(
	    "Naturais e filhos de naturais de territ�rios sob administra��o portuguesa, mas temporariamente ocupados por For�as Armadas e Estados Estrangeiros"),

    REA09(
	    "Estudantes nacionais da Rep�blica de Angola, n�o bolseiros e que n�o tenham residido em territ�rio portugu�s durante a aquisi��o da habilita��o precedente ao 12� ano de escolaridade"),

    VAG01(
	    "Vagas Adicionais - Vagas que s�o necess�rias criar por erros de servi�os do Minist�rio da Educa��o (Direc��o Geral de Acesso ao Ensino Superior)"),

    CIA2C("Concurso Interno de Acesso ao 2� Ciclo"),

    CIA3C("Concurso Interno de Acesso ao 3� Ciclo"),

    PMT("Permuta"),

    MCI("Mudan�a de Curso Interna"),

    MCE("Mudan�a de Curso Externa"),

    RI("Reingresso (de Pr�-Bolonha)"),

    TPB("Transi��o para Bolonha"),

    DA1C("Acesso directo do 1� Ciclo"),

    AG1C("Atribui��o de Grau de 1� Ciclo"),

    CM23("Maiores de 23"),

    TRF("Transfer�ncia"),
    
    MITP("MIT-Portugal");

    String description;

    private Ingression(String description) {
	this.description = description;
    }

    public String getName() {
	return name();
    }

    public String getDescription() {
	return (description.length() > 50 ? description.substring(0, 49) + " ..." : description);
    }

    public String getFullDescription() {
	return description;
    }

    public boolean hasEntryPhase() {
	return this.equals(Ingression.CNA01) || this.equals(Ingression.PMT);
    }

}

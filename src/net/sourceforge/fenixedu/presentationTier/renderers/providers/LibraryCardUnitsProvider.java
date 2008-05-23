package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class LibraryCardUnitsProvider implements DataProvider {
    
    public Object provide(Object source, Object currentValue) {
        List<String> unitsNames = new ArrayList<String>();
        unitsNames.add("1 - DECivil");
        unitsNames.add("2 - DECivil - Sec. H�d. Rec. H�d. Ambientais");
        unitsNames.add("3 - DECivil - Sec. Mec. Estrutural Estruturas");
        unitsNames.add("4 - DECivil - Sec��o Constru��o");
        unitsNames.add("5 - DECivil - Sec. Urb. Trans. Vias Sistemas");
        unitsNames.add("6 - DECivil - Sec��o Geotecnia");
        unitsNames.add("7 - DECivil - Sec. Sist. Apoio Projecto");
        unitsNames.add("8 - DECivil - Sec��o Arquitectura");
        unitsNames.add("9 - DEEC");
        unitsNames.add("10 - DEEC - �rea Cient�fica Energia");
        unitsNames.add("11 - DEEC - �rea Cient�fica Computadores");
        unitsNames.add("12 - DEEC - �rea Cient�fica Electr�nica");
        unitsNames.add("13 - DEEC - �rea Cient�fica Dec. e Controlo");
        unitsNames.add("14 - DEEC - �rea Cient�fica Telecomunica��es");
        unitsNames.add("15 - DEM");
        unitsNames.add("16 - DEM - Sec��o Projecto Mec�nico");
        unitsNames.add("17 - DEM - Sec��o Termoflu�dos e Energia");
        unitsNames.add("18 - DEM - Sec��o Tecnologia Mec�nica");
        unitsNames.add("19 - DEM - Sec��o Sistemas");
        unitsNames.add("20 - DEM - Sec��o Ambiente e Energia");
        unitsNames.add("21 - DEM - Sec��o Mec�nica Aeroespacial");
        unitsNames.add("22 - DM");
        unitsNames.add("23 - DM - Sec��o Estat�sticas e Aplica��es");
        unitsNames.add("24 - DM - Sec��o Mat. Apli. e An�. Num�rica");
        unitsNames.add("25 - DM - Sec��o L�gica e Computa��o");
        unitsNames.add("26 - DM - Sec��o �lgebra e An�lise");
        unitsNames.add("27 - DEMG");
        unitsNames.add("28 - DEMG - Lab. Mineralogia e Petrologia");
        unitsNames.add("29 - DEMG - Sec��o Explora��o Minas");
        unitsNames.add("30 - DEMG - Lab. Mine. e Pla. Mineiro");
        unitsNames.add("31 - DEQB");
        unitsNames.add("32 - DF");
        unitsNames.add("33 - DEI");
        unitsNames.add("34 - DEG");
        unitsNames.add("35 - SAEN");
        unitsNames.add("36 - DEMAT");

        return unitsNames;
    }

    public Converter getConverter() {
        return null;
    }

}

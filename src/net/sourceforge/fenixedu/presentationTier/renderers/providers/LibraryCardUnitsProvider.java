package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class LibraryCardUnitsProvider implements DataProvider {
    
    public Object provide(Object source, Object currentValue) {
        List<String> unitsNames = new ArrayList<String>();
        unitsNames.add(new String("1 - DECivil"));
        unitsNames.add(new String("2 - DECivil - Sec. H�d. Rec. H�d. Ambientais"));
        unitsNames.add(new String("3 - DECivil - Sec. Mec. Estrutural Estruturas"));
        unitsNames.add(new String("4 - DECivil - Sec��o Constru��o"));
        unitsNames.add(new String("5 - DECivil - Sec. Urb. Trans. Vias Sistemas"));
        unitsNames
                .add(new String("6 - DECivil - Sec��o Geotecnia"));
        unitsNames.add(new String("7 - DECivil - Sec. Sist. Apoio Projecto"));
        unitsNames.add(new String("8 - DECivil - Sec��o Arquitectura"));
        unitsNames.add(new String("9 - DEEC"));
        unitsNames.add(new String("10 - DEEC - �rea Cient�fica Energia"));
        unitsNames.add(new String("11 - DEEC - �rea Cient�fica Computadores"));
        unitsNames.add(new String("12 - DEEC - �rea Cient�fica Electr�nica"));
        unitsNames.add(new String("13 - DEEC - �rea Cient�fica Dec. e Controlo"));
        unitsNames.add(new String("14 - DEEC - �rea Cient�fica Telecomunica��es"));
        unitsNames.add(new String("15 - DEM"));
        unitsNames.add(new String("16 - DEM - Sec��o Projecto Mec�nico"));
        unitsNames.add(new String("17 - DEM - Sec��o Termoflu�dos e Energia"));
        unitsNames.add(new String("18 - DEM - Sec��o Tecnologia Mec�nica"));
        unitsNames.add(new String("19 - DEM - Sec��o Sistemas"));
        unitsNames.add(new String("20 - DEM - Sec��o Ambiente e Energia"));
        unitsNames.add(new String("21 - DEM - Sec��o Mec�nica Aeroespacial"));
        unitsNames.add(new String("22 - DM"));
        unitsNames.add(new String("23 - DM - Sec��o Estat�sticas e Aplica��es"));
        unitsNames.add(new String("24 - DM - Sec��o Mat. Apli. e An�. Num�rica"));
        unitsNames.add(new String("25 - DM - Sec��o L�gica e Computa��o"));
        unitsNames.add(new String("26 - DM - Sec��o �lgebra e An�lise"));
        unitsNames.add(new String("27 - DEMG"));
        unitsNames.add(new String("28 - DEMG - Lab. Mineralogia e Petrologia"));
        unitsNames.add(new String("29 - DEMG - Sec��o Explora��o Minas"));
        unitsNames.add(new String("30 - DEMG - Lab. Mine. e Pla. Mineiro"));
        unitsNames.add(new String("31 - DEQB"));
        unitsNames.add(new String("32 - DF"));
        unitsNames.add(new String("33 - DEI"));
        unitsNames.add(new String("34 - DEG"));
        unitsNames.add(new String("35 - SAEN"));
        unitsNames.add(new String("36 - DEMAT"));

        return unitsNames;
    }

    public Converter getConverter() {
        return null;
    }

}

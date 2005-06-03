package net.sourceforge.fenixedu.domain;

public enum ShiftType {
    TEORICA,

    PRATICA,

    TEORICO_PRATICA,

    LABORATORIAL,

    DUVIDAS,

    RESERVA;
    
    public String getSiglaTipoAula() {
        String value = this.name();
        if(value == ShiftType.TEORICA.name())
            return "T";
        if(value == ShiftType.PRATICA.name())
            return "P";
        if(value == ShiftType.TEORICO_PRATICA.name())
            return "TP";
        if(value == ShiftType.LABORATORIAL.name())
            return "L";
        if(value == ShiftType.DUVIDAS.name())
            return "D";
        if(value == ShiftType.RESERVA.name())
            return "R";
        return "Error: Invalid lesson type";
        }
    
    public String getFullNameTipoAula() {
      String value = this.name();
      if(value == ShiftType.TEORICA.name())
          return "Te�rica";
      if(value == ShiftType.PRATICA.name())
          return "Pr�tica";
      if(value == ShiftType.TEORICO_PRATICA.name())
          return "TeoricoPr�tica";
      if(value == ShiftType.LABORATORIAL.name())
          return "Laboratorial";
      if(value == ShiftType.DUVIDAS.name())
          return "D�vidas";
      if(value == ShiftType.RESERVA.name())
          return "Reserva";
      return "Error: Invalid lesson type";
  }
    public String getName(){
        return name();
    }
  
}


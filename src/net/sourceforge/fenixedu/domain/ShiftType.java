package net.sourceforge.fenixedu.domain;

public enum ShiftType {

    TEORICA,

    PRATICA,

    TEORICO_PRATICA,

    LABORATORIAL,

    DUVIDAS,

    RESERVA,
    
    SEMINARY,
    
    PROBLEMS,
    
    FIELD_WORK,
    
    TRAINING_PERIOD,
    
    TUTORIAL_ORIENTATION;
    
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
        if(value == ShiftType.SEMINARY.name())
            return "S";
        if(value == ShiftType.PROBLEMS.name())
            return "PB";
        if(value == ShiftType.FIELD_WORK.name())
            return "TC";
        if(value == ShiftType.TRAINING_PERIOD.name())
            return "E";
        if(value == ShiftType.TUTORIAL_ORIENTATION.name())
            return "OT";
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
      if(value == ShiftType.SEMINARY.name())
          return "Semin�rio";
      if(value == ShiftType.PROBLEMS.name())
          return "Problemas";
      if(value == ShiftType.FIELD_WORK.name())
          return "Trabalho de Campo";
      if(value == ShiftType.TRAINING_PERIOD.name())
          return "Est�gio";
      if(value == ShiftType.TUTORIAL_ORIENTATION.name())
          return "Orienta��o Tutorial";
      return "Error: Invalid lesson type";
  }
    public String getName(){
        return name();
    }
  
}


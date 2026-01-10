package com.espe.ListoEgsi.enums;

public enum PhaseEnum {
    FASE1("FASE1", "CUEST001"),
    FASE2("FASE2", "CUEST002"),
    FASE3("FASE3", "CUEST003"),
    FASE4("FASE4", "CUEST004"),
    FASE5("FASE5", "CUEST005"),
    FASE6("FASE6", "CUEST006"),
    FASE7("FASE7", "CUEST007"),
    FASE8("FASE8", "CUEST008"),
    FASE9("FASE9", "CUEST009"),
    FASE10("FASE10", "CUEST010"),
    FASE11("FASE11", "CUEST011"),
    FASE12("FASE12", "CUEST012"),
    FASE13("FASE13", "CUEST013"),
    FASE14("FASE14", "CUEST014"),
    FASE15("FASE15", "CUEST015");


    private final String phaseName;
    private final String questionnaireCode;

    public static PhaseEnum fromPhaseName(String phaseName) {
        if (phaseName == null) {
            return null;
        }

        for (PhaseEnum phase : PhaseEnum.values()) {
            if (phase.getPhaseName().equals(phaseName)) {
                return phase;
            }
        }
        return null;
    }
    
    PhaseEnum(String phaseName, String questionnaireCode) {
        this.phaseName = phaseName;
        this.questionnaireCode = questionnaireCode;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public String getQuestionnaireCode() {
        return questionnaireCode;
    }

    public static String[] getAllPhase() {
        PhaseEnum[] phases = PhaseEnum.values();
        String[] phaseNames = new String[phases.length];
        for (int i = 0; i < phases.length; i++) {
            phaseNames[i] = phases[i].getPhaseName();
        }
        return phaseNames;
    }

    public static boolean isValidPhase(String phaseName) {
        return fromPhaseName(phaseName) != null;
    }


}
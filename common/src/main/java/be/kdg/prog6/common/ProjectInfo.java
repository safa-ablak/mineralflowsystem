package be.kdg.prog6.common;

public final class ProjectInfo {
    // Krystal Distribution Group (KdG)
    public static final String KDG = "KdG";
    public static final String PROJECT_NAME = "Mineral Flow";
    public static final String SYSTEM_NAME = KDG + " - " + PROJECT_NAME;

    public static final String ACADEMIC_YEAR = "2024–2025";
    public static final String DEVELOPER = "Hüseyin Safa Ablak";
    public static final String COURSE_NAME = "Programming 6";
    public static final String INSTITUTION_NAME = "Karel de Grote Hogeschool";

    public static final String COPYRIGHT = String.format(
        "© %s %s. %s was developed as part of the %s course at %s.",
        ACADEMIC_YEAR, DEVELOPER, SYSTEM_NAME, COURSE_NAME, INSTITUTION_NAME
    );

    private ProjectInfo() {
        throw new AssertionError("Utility class");
    }
}
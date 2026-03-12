package be.kdg.prog6.common;

public final class ProjectInfo {
    // Krystal Distribution Group (KdG)
    public static final String KDG = "KdG";
    public static final String PROJECT = "Mineral Flow";
    public static final String SYSTEM = KDG + " - " + PROJECT;

    public static final String ACADEMIC_YEAR = "2024–2025";
    public static final String DEVELOPER = "Hüseyin Safa Ablak";
    public static final String COURSE = "Programming 6";
    public static final String INSTITUTION = "Karel de Grote Hogeschool";

    public static final String COPYRIGHT = String.format(
        "© %s %s. %s was developed as part of the %s course at %s.",
        ACADEMIC_YEAR, DEVELOPER, SYSTEM, COURSE, INSTITUTION
    );

    private ProjectInfo() {
        throw new AssertionError("Utility class");
    }
}
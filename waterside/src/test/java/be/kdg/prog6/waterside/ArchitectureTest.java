package be.kdg.prog6.waterside;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "be.kdg.prog6.waterside", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {
    private static final String DOMAIN_LAYER = "be.kdg.prog6.waterside.domain..";
    private static final String ADAPTER_LAYER = "be.kdg.prog6.waterside.adapter..";
    private static final String CORE_LAYER = "be.kdg.prog6.waterside.core..";
    private static final String PORT_LAYER = "be.kdg.prog6.waterside.port..";

    @ArchTest
    static final ArchRule domainShouldNotDependOnAnyOtherLayerRule =
        noClasses().that().resideInAPackage(DOMAIN_LAYER)
            .should().dependOnClassesThat().resideInAnyPackage(
                ADAPTER_LAYER,
                PORT_LAYER,
                CORE_LAYER
            )
            .because("This conflicts with hexagonal architecture: Domain should not depend on other layers.");

    @ArchTest
    static final ArchRule domainShouldNotDependOnFrameworksRule =
        noClasses().that().resideInAPackage(DOMAIN_LAYER)
            .should().dependOnClassesThat().resideInAnyPackage(
                "org.springframework..",
                "jakarta.persistence..",
                "org.hibernate.."
            )
            .because("Domain must remain framework-agnostic.");

    @Test
    void givenApplicationClasses_thenNoLayerViolationsShouldExist() {
        final JavaClasses jc = new ClassFileImporter().importPackages("be.kdg.prog6.waterside");
        final Architectures.LayeredArchitecture arch = layeredArchitecture().consideringOnlyDependenciesInLayers()
            .layer("DRIVING_ADAPTERS").definedBy("..adapter.in..")
            .layer("DRIVING_PORTS").definedBy("..port.in..")
            .layer("CORE").definedBy("..core..")
            .whereLayer("DRIVING_ADAPTERS").mayNotBeAccessedByAnyLayer()
            .whereLayer("DRIVING_PORTS").mayOnlyBeAccessedByLayers("DRIVING_ADAPTERS", "CORE");
        arch.check(jc);
    }

    @ArchTest
    static final ArchRule commandClassesShouldBeInCorrectPackageRule =
        classes()
            .that().haveSimpleNameEndingWith("Command")
            .should().resideInAPackage("..port.in.command..")
            .because("All command classes must be placed in the '..port.in.command..' package.");

    @ArchTest
    static final ArchRule commandClassesInCorrectPackageShouldBeSuffixedRule =
        classes()
            .that().resideInAPackage("..port.in.command..")
            .should().haveSimpleNameEndingWith("Command")
            .because("Classes in the '..port.in.command..' package must end with 'Command'.");
}

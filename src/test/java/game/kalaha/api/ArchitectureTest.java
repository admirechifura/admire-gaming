package game.kalaha.api;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import game.kalaha.util.ArchitectureRules;

@AnalyzeClasses(packages = "game.kalaha", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitectureTest {

    @ArchTest
    private static final ArchRule common = ArchitectureRules.ALL_DEFAULT_RULES;
}

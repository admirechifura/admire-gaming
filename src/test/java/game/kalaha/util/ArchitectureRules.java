package game.kalaha.util;

import com.tngtech.archunit.lang.CompositeArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

import java.util.Arrays;

public class ArchitectureRules {

    public static final CompositeArchRule ALL_DEFAULT_RULES;

    private ArchitectureRules(){}

    static {
        ALL_DEFAULT_RULES = CompositeArchRule.of(Arrays.asList(GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME,GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS));
    }
}

package com.alxan.noteefy.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TagRegistryUnitTest {
    @Test
    public void shouldHaveTags() {
        TagRegistry tagRegistry = new TagRegistry();
        String tag1 = "Tag1";
        String tag2 = "Tag2";
        tagRegistry.addTag(tag1);
        tagRegistry.addTag(tag2);

        Assertions.assertTrue(tagRegistry.hasTag(tag1));
        Assertions.assertTrue(tagRegistry.hasTag(tag2));
    }
}

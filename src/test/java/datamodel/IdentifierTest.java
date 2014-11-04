package datamodel;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * Created on 2014-11-03.
 */
public class IdentifierTest {

    @Test
    public void testIdentifierNameIsCaseInsensitive() {
        String ident1 = "tableAA";
        String ident2 = "tableaa";
        Assert.assertEquals(new Identifier(ident1), new Identifier(ident2));
        Assert.assertEquals(new Identifier(ident1).hashCode(), new Identifier(ident2).hashCode());
    }

    @Test
    public void testOnlyAlphanumericAndUnderScoreIsAllowed() {
        try {
            new Identifier("WEED464bhbtk543");
            new Identifier("rgm4t545efeFCWER545");
            new Identifier("_rgm4t545efeFCWER545");
            new Identifier("_rgm4t545efeFCWE_R545");
        } catch (IllegalArgumentException e) {
            fail("Identifier should be successfully created");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpaceNotAllowed() {
        new Identifier("Table XX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonAlphanumericNotAllowed() {
        new Identifier("Table$XX");
    }
}

package sets;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

/**
 * FiniteSetTest is a glassbox test of the FiniteSet class.
 */
public class FiniteSetTest {

  /** Test creating sets. */
  @Test
  public void testCreation() {
    assertEquals(Arrays.asList(),
        FiniteSet.of(new float[0]).getPoints());         // empty
    assertEquals(Arrays.asList(1f),
        FiniteSet.of(new float[] {1}).getPoints());      // one item
    assertEquals(Arrays.asList(1f, 2f),
        FiniteSet.of(new float[] {1, 2}).getPoints());   // two items
    assertEquals(Arrays.asList(1f, 2f),
        FiniteSet.of(new float[] {2, 1}).getPoints());   // two out-of-order
    assertEquals(Arrays.asList(-2f, 2f),
        FiniteSet.of(new float[] {2, -2}).getPoints());  // negative
  }

  // Some example sets used by the tests below.
  private static FiniteSet S0 = FiniteSet.of(new float[0]);
  private static FiniteSet S1 = FiniteSet.of(new float[] {1});
  private static FiniteSet S12 = FiniteSet.of(new float[] {1, 2});

  /** Test set equality. */
  @Test
  public void testEquals() {
    assertTrue(S0.equals(S0));
    assertFalse(S0.equals(S1));
    assertFalse(S0.equals(S12));

    assertFalse(S1.equals(S0));
    assertTrue(S1.equals(S1));
    assertFalse(S1.equals(S12));

    assertFalse(S12.equals(S0));
    assertFalse(S12.equals(S1));
    assertTrue(S12.equals(S12));
  }

  /** Test set size. */
  @Test
  public void testSize() {
    assertEquals(S0.size(), 0);
    assertEquals(S1.size(), 1);
    assertEquals(S12.size(), 2);
  }

  private static FiniteSet S2 = FiniteSet.of(new float[] {2});
  private static FiniteSet S3 = FiniteSet.of(new float[] {3});
  private static FiniteSet S23 = FiniteSet.of(new float[] {2, 3});
  private static FiniteSet S34 = FiniteSet.of(new float[] {3, 4});
  private static FiniteSet S123 = FiniteSet.of(new float[] {1, 2, 3});
  private static FiniteSet S1234 = FiniteSet.of(new float[] {1, 2, 3, 4});

  /** Tests forming the union of two finite sets. */
  @Test
  public void testUnion() {
    assertEquals(S0.union(S0), S0);
    assertEquals(S0.union(S1), S1);
    assertEquals(S1.union(S0), S1);
    assertEquals(S0.union(S12), S12);
    assertEquals(S12.union(S0), S12);

    assertEquals(S1.union(S1), S1);
    assertEquals(S1.union(S2), S12);
    assertEquals(S2.union(S1), S12);

    assertEquals(S1.union(S12), S12);
    assertEquals(S12.union(S1), S12);
    assertEquals(S1.union(S23), S123);
    assertEquals(S23.union(S1), S123);

    assertEquals(S12.union(S23), S123);
    assertEquals(S23.union(S12), S123);
    assertEquals(S12.union(S34), S1234);
    assertEquals(S34.union(S12), S1234);
  }

  /** Tests forming the intersection of two finite sets. */
  @Test
  public void testIntersection() {
    assertEquals(S0.intersection(S0),S0);
    assertEquals(S0.intersection(S1),S0);
    assertEquals(S1.intersection(S0),S0);
    assertEquals(S0.intersection(S12),S0);
    assertEquals(S12.intersection(S0),S0);

    assertEquals(S1.intersection(S1),S1);
    assertEquals(S1.intersection(S2),S0);
    assertEquals(S2.intersection(S1),S0);

    assertEquals(S1.intersection(S12),S1);
    assertEquals(S12.intersection(S1),S1);
    assertEquals(S1.intersection(S23),S0);
    assertEquals(S23.intersection(S1),S0);

    assertEquals(S12.intersection(S23),S2);
    assertEquals(S23.intersection(S12),S2);
    assertEquals(S12.intersection(S34),S0);
    assertEquals(S34.intersection(S12),S0);
    
  }

  /** Tests forming the difference of two finite sets. */
  @Test
  public void testDifference() {
    assertEquals(S0.difference(S0),S0);
    assertEquals(S0.difference(S1),S0);
    assertEquals(S1.difference(S0),S1);
    assertEquals(S0.difference(S12),S0);
    assertEquals(S12.difference(S0),S12);

    assertEquals(S1.difference(S1),S0);
    assertEquals(S1.difference(S2),S1);
    assertEquals(S2.difference(S1),S2);

    assertEquals(S1.difference(S12),S0);
    assertEquals(S12.difference(S1),S2);
    assertEquals(S1.difference(S23),S1);
    assertEquals(S23.difference(S1),S23);

    assertEquals(S12.difference(S23),S1);
    assertEquals(S23.difference(S12),S3);
    assertEquals(S12.difference(S34),S12);
    assertEquals(S34.difference(S12),S34);
  }

}

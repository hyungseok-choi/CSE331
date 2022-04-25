package sets;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an immutable set of points on the real line that is easy to
 * describe, either because it is a finite set, e.g., {p1, p2, ..., pN}, or
 * because it excludes only a finite set, e.g., R \ {p1, p2, ..., pN}. As with
 * FiniteSet, each point is represented by a Java float with a non-infinite,
 * non-NaN value.
 */
public class SimpleSet {
  /**
   * Creates a simple set containing only the given points.
   * @param vals Array containing the points to make into a SimpleSet
   * @spec.requires points != null and has no NaNs, no infinities, and no dups
   * @spec.effects this = {vals[0], vals[1], ..., vals[vals.length-1]}
   */
  public SimpleSet(float[] vals) {
    points = FiniteSet.of(vals);
    complement = false;
  }

  // Points are stored in a FiniteSet, with an additional field complement
  // which represent whether it is a finite set or a real numbers excluding that finite set.
  //
  // RI: points != null
  // AF(this) = R \ {points.getPoints()} if complement else {points.getPoints()}
  private FiniteSet points;
  private boolean complement;

  /**
   * Private constructor that directly fills in the fields above.
   * @param complement Whether this = points or this = R \ points
   * @param points List of points that are in the set or not in the set
   * @spec.requires points != null
   * @spec.effects this = R \ points if complement else points
   */
  private SimpleSet(boolean complement, FiniteSet points) {
    this.complement = complement;
    this.points = points;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleSet))
      return false;

    SimpleSet other = (SimpleSet) o;
    return this.points.equals(other.points) && this.complement == other.complement;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Returns the number of points in this set.
   * @return N      if this = {p1, p2, ..., pN} and
   *         infty  if this = R \ {p1, p2, ..., pN}
   */
  public float size() {
    return this.complement ? Float.POSITIVE_INFINITY : this.points.size();
  }

  /**
   * Returns a string describing the points included in this set.
   * @return the string "R" if this contains every point,
   *     a string of the form "R \ {p1, p2, .., pN}" if this contains all
   *        but {@literal N > 0} points, or
   *     a string of the form "{p1, p2, .., pN}" if this contains
   *        {@literal N >= 0} points,
   *     where p1, p2, ... pN are replaced by the individual numbers.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    List<Float> vals = this.points.getPoints();
    int i = 0;

    // for the special case when we print only "R"
    if (vals.size() == 0 && this.complement) return "R";

    // append each element at a time.
    sb.append("{");
    // Inv: sb = "{" + vals[0] + ", " + vals[1] + ", " + ...+ vals[i-1]"
    while(i != vals.size()){
      sb.append(vals.get(i));
      if (i < vals.size() - 1) sb.append(", ");
      i = i + 1;
    }

    sb.append("}");

    // add additional part if this represents R \ {points}
    if (this.complement){
      sb.insert(0, "R \\ ");
    }

    return sb.toString();
  }

  /**
   * Returns a set representing the points R \ this.
   * @return R \ this
   */
  public SimpleSet complement() {
    // By changing the sign of the complement, we get R \ this.
    return new SimpleSet(!this.complement, this.points);
  }

  /**
   * Returns the union of this and other.
   * @param other Set to union with this one.
   * @spec.requires other != null
   * @return this union other
   */
  public SimpleSet union(SimpleSet other) {
    FiniteSet S = this.points;
    FiniteSet T = other.points;
    FiniteSet U;

    if (this.complement && other.complement){
      // when both S and T represent a set of points that excluded,
      // elements that are not on both sets will be covered by one another.
      // In other words, an intersection set of both sets will be excluded from R after we get a union set of them.
      U = S.intersection(T);
    } else if (this.complement){
      // when S represents set of points that excluded and T represents set of points,
      // a union set of both SimpleSet will be (R \ {S.getPoint()}) + {T.getPoint()}
      // In other words, only the difference set of S and T will be excluded from R when we get union set of S and T.
      U = S.difference(T);
    } else if (other.complement){
      // vice versa from the above case.
      U = T.difference(S);
    } else {
      // when both S and T represent a set of points, we get a union set of those by using the union method of FiniteSet.
      U = S.union(T);
    }

    // if at least one of this.complement and other.complement is True, a new SimpleSet represents a set of points to be excluded.
    return new SimpleSet(this.complement || other.complement, U);
  }

  /**
   * Returns the intersection of this and other.
   * @param other Set to intersect with this one.
   * @spec.requires other != null
   * @return this intersect other
   */
  public SimpleSet intersection(SimpleSet other) {
    FiniteSet S = this.points;
    FiniteSet T = other.points;
    FiniteSet U;

    if (this.complement && other.complement){
      // when both S and T represent a set of points that excluded,
      // each element in S and T will be excluded.
      // In other words, a union set of both sets will be excluded from R after we get an intersection set of them.
      U = S.union(T);
    } else if (this.complement){
      // when S represents set of points that excluded and T represents set of points,
      // an intersection set of both SimpleSet will be {T.getPoint()} - {S.getPoint()}
      // In other words, only the difference set of T and S will remain when we get the intersection set of S and T.
      U = T.difference(S);
    } else if (other.complement){
      // vice versa from the above case.
      U = S.difference(T);
    } else {
      // when both S and T represent a set of points, we get an intersection set of those by using the intersection method of FiniteSet.
      U = S.intersection(T);
    }

    // if both this.complement and other.complement are True, a new SimpleSet represents a set of points to be excluded.
    return new SimpleSet(this.complement && other.complement, U);
  }

  /**
   * Returns the difference of this and other.
   * @param other Set to difference from this one.
   * @spec.requires other != null
   * @return this minus other
   */
  public SimpleSet difference(SimpleSet other) {
    FiniteSet S = this.points;
    FiniteSet T = other.points;
    FiniteSet U;

    if (this.complement && other.complement){
      // when both S and T represent a set of points that excluded,
      // each element in S and T will be excluded.
      // In other words, a difference set of T and S will be excluded from R after we get a difference set of them.
      U = T.difference(S);
    } else if (this.complement){
      // when S represents set of points that excluded and T represents set of points,
      // a difference set of S and T will be R \ {S.getValue()} - {T.getValue()}
      // In other words, the union set of S and T will be excluded from R when we get the difference set of S and T.
      U = S.union(T);
    } else if (other.complement){
      // when T represents set of points that excluded and S represents set of points,
      // a difference set of both SimpleSet will be {S.getPoint()} - {R \ T.getPoint()}
      // In other words, only the intersection set of S and T will remain when we get the difference set of S and T.
      U = S.intersection(T);
    } else {
      // when both S and T represent a set of points, we get a difference set of those by using the difference method of FiniteSet.
      U = S.difference(T);
    }

    // Only then this.complement is a True and other.complement is a False, a new SimpleSet represents a set of points to be excluded.
    return new SimpleSet(this.complement && !other.complement, U);
  }
}

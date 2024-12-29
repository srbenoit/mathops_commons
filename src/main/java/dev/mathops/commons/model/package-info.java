/**
 * A generalized data model consisting of a tree of nodes in the spirit of XML, HTML, and DOM.  Every node stores a map
 * from typed key to attribute, property, and data values.  An XML/HTML/DOM document can be represented faithfully in
 * this tree model, but the model is more general.
 *
 * <p>
 * Every typed value must support a faithful String representation, and the typed keys used in the map include a codec
 * to encode/decode typed values.  Values can be set or retrieved in typed form or string form to better support
 * serialization.
 */
package dev.mathops.commons.model;

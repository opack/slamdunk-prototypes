/**
 * Contient les composants basics de GameParts.
 * Les composants peuvent être séparés en 2 blocs purement conceptuels :
 *   - Les parties (-Part) : représentent un morceau "tangible" du GameObject.
 *   Les Parts contiennent essentiellement des données et ont une représentation
 *   directe et concrète sur le monde, ce qui implique qu'elles sont essentiellement
 *   les seuls composants à implémenter la méthode render(). Pour résumer, les
 *   Parts "sont" quelque chose.
 *   - Les scripts (-Script) : représentent un comportement, une logique
 *   applicative du GameObject. Les Scripts contiennent essentiellement
 *   des algos de manipulation de Parts. Pour résumer, les
 *   Scripts "font" quelque chose.
 */
package com.slamdunk.toolkit.gameparts.components;
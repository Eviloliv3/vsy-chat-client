
package de.vsy.client.gui.essential_graphical_unit;

/**
 * States indicating the current initialization/authentication dialog.
 */
public enum NavigationGoal {
  INITIAL("INITIAL"),
  LOGIN("LOGIN"),
  ACCOUNT_CREATION("ACCOUNT_CREATION"),
  LOGOUT("LOGOUT"),
  ACCOUNT_DELETION("ACCOUNT_DELETION"),
  CONTACT_ADDITION("CONTACT_ADDITION"),
  CONTACT_REMOVAL("CONTACT_REMOVAL"),
  CLOSE_APPLICATION("CLOSE_APPLICATION");

  private final String goalAsString;

  NavigationGoal(final String stringVersion){
    this.goalAsString = stringVersion;
  }

  @Override
  public String toString(){ return this.goalAsString; }
}

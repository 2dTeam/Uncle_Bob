package org.team2d.uncle_bob;

// TODO: need to rename to make a bit more sense. It doesn't look like OOP.
public class SomeUtils {

    public int getResourceId(String VariableName, String Resourcename, String PackageName) {
        try {
            return MainActivity.getResources().getIdentifier(VariableName, Resourcename, PackageName);
        } catch (final Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}

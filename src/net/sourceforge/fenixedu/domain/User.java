package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

/**
 * @author mrsp
 * @author shezad
 */

public class User extends User_Base {

    final public static Comparator<User> USER_UID_COMPARATOR = new Comparator<User>() {
        public int compare(User u1, User u2) {
            if (u1.getUserUId() == null && u2.getUserUId() == null) {
                return 0;
            }
            if (u1.getUserUId() == null) {
                return -1;
            }
            if (u2.getUserUId() == null) {
                return 1;
            }
            String u1Id = u1.getUserUId().replaceFirst("[a-zA-Z]+", "");
            String u2Id = u2.getUserUId().replaceFirst("[a-zA-Z]+", "");
            return Integer.valueOf(u1Id).compareTo(Integer.valueOf(u2Id));
        }
    };

    public User() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public User(Person person) {
        this();
        this.setPerson(person);
    }

    public static User readUserByUserUId(final String userUId) {
        for (final User user : RootDomainObject.getInstance().getUsers()) {
            if (user.getUserUId() != null && user.getUserUId().equalsIgnoreCase(userUId)) {
                return user;
            }
        }
        return null;
    }

    public Login readUserLoginIdentification() {

        // / In present exist only one Person Login Identification
        for (Identification identification : this.getIdentifications()) {
            if (identification instanceof Login) {
                return (Login) identification;
            }
        }
        return null;
    }
}

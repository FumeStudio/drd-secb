package com.secb.android.controller.manager;


import com.secb.android.model.User;

import net.comptoirs.android.common.helper.Logger;

public class UserManager {
    private static final String TAG = "UserManager";
    public User user;


    private static UserManager _instance = new UserManager();

    public static UserManager getInstance() {
        return _instance;
    }

    private UserManager() {
        Logger.instance().v(TAG, "Cached User Reloaded");
        user = CachingManager.getInstance().loadUser();
    }



    public User getUser() {
        return user;
    }


    public void saveUser(User user) {
        this.user = user;
        CachingManager.getInstance().saveUser(user);
    }
    /*
     * Logout user
     */
    public void logout() {
//        this.user.isUserLoggedOut = true;
        CachingManager.getInstance().deleteUser();
        user = null;
    }
    /*
     * Reset User
     */
    public void resetUser() {
        CachingManager.getInstance().deleteUser();
        user = null;
    }
}

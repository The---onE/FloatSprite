package com.xmx.floatsprite.Tools.Data.Cloud;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.xmx.floatsprite.Tools.Data.Callback.DelCallback;
import com.xmx.floatsprite.Tools.Data.Callback.InsertCallback;
import com.xmx.floatsprite.Tools.Data.Callback.SelectCallback;
import com.xmx.floatsprite.Tools.Data.Callback.UpdateCallback;
import com.xmx.floatsprite.Tools.Data.DataConstants;
import com.xmx.floatsprite.User.Callback.AutoLoginCallback;
import com.xmx.floatsprite.User.UserConstants;
import com.xmx.floatsprite.User.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class BaseCloudEntityManager<Entity extends ICloudEntity> {

    //子类构造函数中初始化下列变量！
    protected String tableName = null;
    protected Entity entityTemplate = null; //空模版，不需要数据
    protected String userField = null; //用户字段，保存当前登录用户的ObjectId，为空时不保存用户字段

    protected boolean checkDatabase() {
        return tableName != null && entityTemplate != null;
    }

    //查询全部数据并排序, ascFlag为true升序，为false降序
    public void selectByCondition(final Map<String, Object> conditions,
                                  final String order, final boolean ascFlag,
                                  final SelectCallback<Entity> callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final AVObject user) {
                AVQuery<AVObject> query = new AVQuery<>(tableName);
                if (userField != null) {
                    query.whereEqualTo(userField, user.getObjectId());
                }
                if (conditions != null) {
                    for (String key : conditions.keySet()) {
                        query.whereEqualTo(key, conditions.get(key));
                    }
                }
                if (order != null) {
                    if (ascFlag) {
                        query.orderByAscending(order);
                    } else {
                        query.orderByDescending(order);
                    }
                }
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        if (e == null) {
                            List<Entity> entities = new ArrayList<>();
                            for (AVObject object : avObjects) {
                                Entity entity = (Entity) entityTemplate.convertToEntity(object);
                                entities.add(entity);
                            }
                            callback.success(user, entities);
                        } else {
                            callback.syncError(e);
                        }
                    }
                });
            }

            @Override
            public void error(AVException e) {
                callback.syncError(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                        callback.syncError(DataConstants.NOT_LOGGED_IN);
                        break;
                    case UserConstants.USERNAME_ERROR:
                        callback.syncError(DataConstants.USERNAME_ERROR);
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        callback.syncError(DataConstants.CHECKSUM_ERROR);
                        break;
                }
            }
        });
    }

    public void insertToCloud(final Entity entity, final InsertCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final AVObject user) {
                final AVObject object = entity.getContent(tableName);
                if (userField != null) {
                    object.put(userField, user.getObjectId());
                }
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            callback.success(user, object.getObjectId());
                        } else {
                            callback.syncError(e);
                        }
                    }
                });
            }

            @Override
            public void error(AVException e) {
                callback.syncError(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                        callback.syncError(DataConstants.NOT_LOGGED_IN);
                        break;
                    case UserConstants.USERNAME_ERROR:
                        callback.syncError(DataConstants.USERNAME_ERROR);
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        callback.syncError(DataConstants.CHECKSUM_ERROR);
                        break;
                }
            }
        });
    }

    public void deleteFromCloud(final String objectId, final DelCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final AVObject user) {
                AVQuery<AVObject> query = new AVQuery<>(tableName);
                query.getInBackground(objectId, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject object, AVException e) {
                        if (e == null) {
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        callback.success(user);
                                    } else {
                                        callback.syncError(e);
                                    }
                                }
                            });
                        } else {
                            callback.syncError(e);
                        }
                    }
                });
            }

            @Override
            public void error(AVException e) {
                callback.syncError(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                        callback.syncError(DataConstants.NOT_LOGGED_IN);
                        break;
                    case UserConstants.USERNAME_ERROR:
                        callback.syncError(DataConstants.USERNAME_ERROR);
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        callback.syncError(DataConstants.CHECKSUM_ERROR);
                        break;
                }
            }
        });
    }

    public void updateToCloud(final String objectId, final Map<String, Object> update,
                              final UpdateCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final AVObject user) {
                AVQuery<AVObject> query = new AVQuery<>(tableName);
                query.getInBackground(objectId, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject object, AVException e) {
                        if (e == null) {
                            if (update != null) {
                                for (String key : update.keySet()) {
                                    object.put(key, update.get(key));
                                }
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            callback.success(user);
                                        } else {
                                            callback.syncError(e);
                                        }
                                    }
                                });
                            }
                        } else {
                            callback.syncError(e);
                        }
                    }
                });
            }

            @Override
            public void error(AVException e) {
                callback.syncError(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                        callback.syncError(DataConstants.NOT_LOGGED_IN);
                        break;
                    case UserConstants.USERNAME_ERROR:
                        callback.syncError(DataConstants.USERNAME_ERROR);
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        callback.syncError(DataConstants.CHECKSUM_ERROR);
                        break;
                }
            }
        });
    }
}

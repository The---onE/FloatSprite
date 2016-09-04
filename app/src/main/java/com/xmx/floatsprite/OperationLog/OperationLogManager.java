package com.xmx.floatsprite.OperationLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_onE on 2016/2/24.
 */
public class OperationLogManager {
    private static OperationLogManager instance;

    long sqlVersion = 0;
    long version = System.currentTimeMillis();
    List<OperationLog> sqlList = new ArrayList<>();

    public synchronized static OperationLogManager getInstance() {
        if (null == instance) {
            instance = new OperationLogManager();
        }
        return instance;
    }

    public List<OperationLog> getData() {
        return sqlList;
    }

    public long updateData() {
        OperationLogEntityManager sqlManager = OperationLogEntityManager.getInstance();
        if (sqlManager.getVersion() != sqlVersion) {
            sqlVersion = sqlManager.getVersion();

            sqlList.clear();
            sqlList = sqlManager.selectAll("Time", false);

            version++;
        }
        return version;
    }
}

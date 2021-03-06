package password.pwm.util.localdb;

import password.pwm.PwmApplication;
import password.pwm.config.option.DataStorageMethod;
import password.pwm.error.PwmException;
import password.pwm.health.HealthRecord;
import password.pwm.svc.PwmService;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LocalDBService implements PwmService {
    private PwmApplication pwmApplication;

    @Override
    public STATUS status() {
        if ( pwmApplication != null
                && pwmApplication.getLocalDB() != null
                && pwmApplication.getLocalDB().status() == LocalDB.Status.OPEN)
        {
            return STATUS.OPEN;
        }

        return STATUS.CLOSED;
    }

    @Override
    public void init(final PwmApplication pwmApplication) throws PwmException {
        this.pwmApplication = pwmApplication;
    }

    @Override
    public void close() {
        //no-op
    }

    @Override
    public List<HealthRecord> healthCheck() {
        return null;
    }

    @Override
    public ServiceInfoBean serviceInfo() {
        final Map<String,String> returnInfo = new LinkedHashMap<>();
        if (status() == STATUS.OPEN) {
            final Map<String,Serializable> localDbInfo = pwmApplication.getLocalDB().debugInfo();
            for (final Map.Entry<String,Serializable> entry : localDbInfo.entrySet()) {
                returnInfo.put( entry.getKey(), String.valueOf(entry.getValue()) );
            }
        }
        return new ServiceInfoBean(Collections.singleton(DataStorageMethod.LOCALDB), Collections.unmodifiableMap(returnInfo));
    }
}

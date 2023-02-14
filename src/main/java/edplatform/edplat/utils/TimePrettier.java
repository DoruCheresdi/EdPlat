package edplatform.edplat.utils;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class TimePrettier {

    public String prettyTimestamp(Timestamp createdAt) {
        PrettyTime t = new PrettyTime(new Date(System.currentTimeMillis()));
        return t.format(new Date(createdAt.getTime()));
    }
}

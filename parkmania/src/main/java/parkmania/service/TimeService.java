package parkmania.service;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.time.*;

/**
 * Fournit une notion de temps centrée sur l'application qui peut être accélérée.
 * Le principe : on conserve une base réelle (Instant baseReal) et une base virtuelle
 * (LocalDateTime baseVirtual) ; getNow() retourne baseVirtual + (nowReal - baseReal) * multiplier.
 */
@Singleton
@Startup
public class TimeService {

    private volatile double multiplier;

    private volatile Instant baseReal;
    private volatile LocalDateTime baseVirtual;
    private final ZoneId zone = ZoneId.systemDefault();

    @PostConstruct
    public void init() {
        this.multiplier = 1.0;
        this.baseReal = Instant.now();
        this.baseVirtual = LocalDateTime.now();
    }

    public synchronized void setMultiplier(double m) {
        // Update bases so the virtual time remains continuous
        LocalDateTime currentVirtual = getNow();
        this.baseVirtual = currentVirtual;
        this.baseReal = Instant.now();
        this.multiplier = m;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public LocalDateTime getNow() {
        Instant nowReal = Instant.now();
        Duration elapsedReal = Duration.between(baseReal, nowReal);

        // Multiply elapsed by multiplier (double) -> convert to nanos for precision
        double nanos = elapsedReal.toNanos();
        double scaledNanos = nanos * multiplier;
        long scaledNanosLong = (long) scaledNanos;

        Instant virtualInstant = baseVirtual.atZone(zone).toInstant().plusNanos(scaledNanosLong);
        return LocalDateTime.ofInstant(virtualInstant, zone);
    }
}

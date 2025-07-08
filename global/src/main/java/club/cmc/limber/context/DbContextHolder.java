package club.cmc.limber.context;

public class DbContextHolder {
    private static final ThreadLocal<DbType> context = new ThreadLocal<>();

    public static void set(DbType dbType) {
        context.set(dbType);
    }

    public static DbType get() {
        return context.get() != null ? context.get() : DbType.WRITE;
    }

    public static void clear() {
        context.remove();
    }
}

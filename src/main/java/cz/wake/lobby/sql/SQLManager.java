package cz.wake.lobby.sql;

import com.zaxxer.hikari.HikariDataSource;
import cz.wake.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLManager {

    private final Main plugin;
    private final ConnectionPoolManager pool;
    private HikariDataSource dataSource;
    static final Logger log = LoggerFactory.getLogger(SQLManager.class);

    public SQLManager(Main plugin) {
        this.plugin = plugin;
        pool = new ConnectionPoolManager(plugin);
    }

    public void onDisable() {
        pool.closePool();
    }

    public ConnectionPoolManager getPool() {
        return pool;
    }

    public final boolean hasData(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM craftboxer_buys WHERE uuid = '" + uuid.toString() + "';");
            ps.executeQuery();
            return ps.getResultSet().next();
        } catch (Exception e) {
            log.error("", e);
            return false;
        } finally {
            pool.close(conn, ps, null);
        }
    }

    public final int getCraftCoins(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT balance FROM CraftCoins WHERE uuid = '" + uuid.toString() + "';");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("balance");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBoxes(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT pocet FROM craftboxer WHERE uuid = '" + uuid.toString() + "';");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("pocet");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final long getNextboxTime(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT dalsiBox FROM craftboxer WHERE uuid = '" + uuid.toString() + "';");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getLong("dalsiBox");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0L;
    }

    public final boolean hasBoxData(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM craftboxer WHERE uuid = '" + uuid.toString() + "';");
            ps.executeQuery();
            return ps.getResultSet().next();
        } catch (Exception e) {
            log.error("", e);
            return false;
        } finally {
            pool.close(conn, ps, null);
        }
    }

    public final long getFullResetTime() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT time FROM craftboxer_nextReset WHERE id = 1;");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getLong("time");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0L;
    }

    public final long getTimeToBuy(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT time FROM craftboxer_buys WHERE uuid = '" + uuid.toString() + "';");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getLong("time");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0L;
    }

    public final int getSkyKeysDust(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT skykeys FROM CraftCoins WHERE uuid = '" + uuid.toString() + "';");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("skykeys");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getVotesWeek(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT week FROM votes WHERE uuid = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("week");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getVotesMonth(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT month FROM votes WHERE uuid = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("month");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getVotesAll(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT votes FROM votes WHERE uuid = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("votes");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getAnniPlayedGames(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT PlayedGames FROM annihilation_stats WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("PlayedGames");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getAnniMinedDiamond(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT MinedDiamond FROM annihilation_stats WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("MinedDiamond");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getAnniKillBoss(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT KillBoss FROM annihilation_stats WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("KillBoss");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getAnniDestroyed(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT Destroyed FROM annihilation_stats WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("Destroyed");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getAnniLosses(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT Losses FROM annihilation_stats WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("Losses");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getAnniWins(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT Wins FROM annihilation_stats WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("Wins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getAnniDeaths(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT Deaths FROM annihilation_stats WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("Deaths");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getAnniKills(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT Kills FROM annihilation_stats WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("Kills");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getSkyWarsPlayed(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT normal_played FROM skywars_players WHERE player_uuid = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("normal_played");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getSkyWarsDeaths(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT normal_deaths FROM skywars_players WHERE player_uuid = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("normal_deaths");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getSkyWarsWins(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT normal_wins FROM skywars_players WHERE player_uuid = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("normal_wins");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getSkyWarsKills(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT normal_kills FROM skywars_players WHERE player_uuid = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("normal_kills");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBedwarsKills(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT kills FROM bw_stats_players WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("kills");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBedwarsWins(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT wins FROM bw_stats_players WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("wins");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBedwarsScore(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT score FROM bw_stats_players WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBedwarsLoses(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT loses FROM bw_stats_players WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("loses");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBedwarsDeaths(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT deaths FROM bw_stats_players WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("deaths");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBedwarsDestroyed(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT destroyedBeds FROM bw_stats_players WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("destroyedBeds");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBedwarsPlayed(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT games FROM bw_stats_players WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("games");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getDrawitScore(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT points FROM GuessNDraw WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("points");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getDrawitWins(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT victories FROM GuessNDraw WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("victories");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getDrawitLoses(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT losses FROM GuessNDraw WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("losses");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getDrawitRightGuess(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT right_guesses FROM GuessNDraw WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("right_guesses");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getDrawitWrongGuess(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT wrong_guesses FROM GuessNDraw WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("wrong_guesses");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getDrawitPlayed(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT plays FROM GuessNDraw WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("plays");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBuildBattleWins(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT Wins FROM masterbuilders WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("Wins");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getBuildBattlePlayedGames(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT PlayedGames FROM masterbuilders WHERE UUID = '" + uuid.toString() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("PlayedGames");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final void createRecordBuy(final Player p, final long milis) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO craftboxer_buys (uuid, time) VALUES (?,?);");
                    ps.setString(1, p.getUniqueId().toString());
                    ps.setLong(2, milis);
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void updateTimeBuy(final Player p, final long time) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE craftboxer_buys SET time = ? WHERE uuid = '" + p.getUniqueId().toString() + "';");
                    ps.setLong(1, time);
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void takeCoins(final Player p, final int coins) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE CraftCoins SET balance = ? WHERE uuid = '" + p.getUniqueId().toString() + "';");
                    ps.setInt(1, getCraftCoins(p.getUniqueId()) - coins);
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void takeSkyKeys(final Player p, final int coins) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE CraftCoins SET skykeys = ? WHERE uuid = '" + p.getUniqueId().toString() + "';");
                    ps.setInt(1, getSkyKeysDust(p.getUniqueId()) - coins);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void createRecord(final Player p, final int amount, final long milis) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO craftboxer (nick ,uuid, pocet, dalsiBox) VALUES (?,?,?,?);");
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getUniqueId().toString());
                    ps.setInt(3, amount + getBoxes(p.getUniqueId()));
                    ps.setLong(4, milis);
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void addBoxesWithTime(final Player p, final int amount, final long milis) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO craftboxer (nick ,uuid, pocet, dalsiBox) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE pocet = ?, dalsiBox = ?;");
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getUniqueId().toString());
                    ps.setInt(3, amount);
                    ps.setLong(4, milis);
                    ps.setInt(5, amount + getBoxes(p.getUniqueId()));
                    ps.setLong(6, milis);
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void resetBoxes() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE craftboxer SET pocet = '0';");
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void resetTime(final long time) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE craftboxer_nextReset SET time = ? WHERE id = 1;");
                    ps.setLong(1, time);
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final int getCraftBoxes(final Player p, final String s) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT " + s + " FROM MysteryBoxes WHERE UUID = '" + p.getUniqueId().toString() + "';");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt(s);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final void addCraftBox(final Player p, final String name, final int n) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO MysteryBoxes (UUID , " + name + ") VALUES (?,?) ON DUPLICATE KEY UPDATE " + name + " = ?;");
                    ps.setString(1, p.getUniqueId().toString());
                    ps.setInt(2, n);
                    ps.setInt(3, n + getCraftBoxes(p, name));
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public int getOnlinePlayers(final String server) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT pocet_hracu FROM stav_survival_server WHERE nazev = '" + server + "';");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("pocet_hracu");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final void addSettingsDefault(final Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO player_settings (Nick) VALUES (?) ON DUPLICATE KEY UPDATE Nick = ?;");
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getName());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void updateSettings(final Player p, final String settings, final int value) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE player_settings SET " + settings + " = " + value + " WHERE nick = ?;");
                    ps.setString(1, p.getName());
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final int getSettings(final Player p, final String settings) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT " + settings + " FROM player_settings WHERE nick = '" + p.getName() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt(settings);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final int getMiniGamesStats(final Player p, final String table, final String playerRow, final String stat) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT " + stat + " FROM " + table + " WHERE " + playerRow + " = '" + p.getName() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt(stat);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public int getOnlinePlayersSum(final String table) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT SUM(pocet_hracu) AS total FROM " + table + "");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("total");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public int getMaintenance(final String s) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT udrzba FROM stav_survival_server WHERE nazev = '" + s + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt("udrzba");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final boolean isAT(Player p) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM at_table WHERE nick = '" + p.getName() + "';");
            ps.executeQuery();
            return ps.getResultSet().next();
        } catch (Exception e) {
            log.error("", e);
            return false;
        } finally {
            pool.close(conn, ps, null);
        }
    }

    public final int getAtPlayerTime(Player p, String table) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT " + table + " FROM at_table WHERE nick = '" + p.getName() + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt(table);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final void updateAtLastActive(Player p, long time) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE at_table SET minigames_pos_aktivita = '" + time + "' WHERE nick = ?;");
                    ps.setString(1, p.getName());
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void updateAtPlayerTime(Player p) {
        int cas = Main.getInstance().fetchData().getAtPlayerTime(p, "minigames_played_time") + 1;
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE at_table SET minigames_played_time = '" + cas + "' WHERE nick = ?;");
                    ps.setString(1, p.getName());
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void updateAtPoints(Player p) {
        int cas = Main.getInstance().fetchData().getAtPlayerTime(p, "minigames_chat_body") + 1;
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE at_table SET minigames_chat_body = '" + cas + "' WHERE nick = ?;");
                    ps.setString(1, p.getName());
                    ps.executeUpdate();
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final int getStalkerStats(String p, String stats) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT " + stats + " FROM at_table WHERE nick = ?");
            ps.setString(1, p);
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getInt(stats);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final Long getStalkerStatsTime(String p, String stats) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT " + stats + " FROM at_table WHERE nick = '" + p + "'");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getLong(stats);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return (long) 0;
    }

    public final List<String> getAllAdminTeam() {
        List<String> names = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT nick FROM at_table");
            ps.executeQuery();
            while (ps.getResultSet().next()) {
                names.add(ps.getResultSet().getString(1));
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            pool.close(conn, ps, null);
        }
        return names;
    }
}

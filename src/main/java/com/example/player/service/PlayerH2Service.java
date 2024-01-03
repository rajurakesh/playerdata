package com.example.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.player.model.PlayerRowMapper;
import com.example.player.model.Player;
import com.example.player.repository.PlayerRepository;
import java.util.*;

@Service
public class PlayerH2Service implements PlayerRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getPlayers() {

        List<Player> playerList = db.query("select * from TEAM", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override
    public Player getPlayerById(int playerId) {
        try {
            Player player = db.queryForObject("SELECT * FROM TEAM WHERE playerId = ?", new PlayerRowMapper(), playerId);
            return player;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Player addPlayer(Player player) {
        db.update("INSERT INTO TEAM(playerId,playerName,jerseyNumber,role) values(?,?,?,?)",
                player.getPlayerId(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());

        Player savedPlayer = db.queryForObject("SELECT * FROM TEAM WHERE playerId = ?", new PlayerRowMapper(),
                player.getPlayerId());
        return savedPlayer;
    }

}

// Write your code here
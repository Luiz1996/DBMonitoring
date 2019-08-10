/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;

/**
 *
 * @author luiz.pereira
 */
public class Transacao {
    String serverPid;
    String dtHora;
    String ip;
    String user;
    String pageFetches;

    public Transacao(){}

    public Transacao(String serverPid, String dtHora, String ip, String user, String pageFetches) {
        this.serverPid = serverPid;
        this.dtHora = dtHora;
        this.ip = ip;
        this.user = user;
        this.pageFetches = pageFetches;
    }
    
    public String getServerPid() {
        return serverPid;
    }

    public void setServerPid(String serverPid) {
        this.serverPid = serverPid;
    }

    public String getDtHora() {
        return dtHora;
    }

    public void setDtHora(String dtHora) {
        this.dtHora = dtHora;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    } 

    public String getPageFetches() {
        return pageFetches;
    }

    public void setPageFetches(String pageFetches) {
        this.pageFetches = pageFetches;
    }
}

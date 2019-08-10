/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import conexao.Conexao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.mail.MessagingException;
import model.Email;
import model.Transacao;

/**
 *
 * @author luiz.pereira
 */
public class Main {
    public static void main(String[] args) throws SQLException, MessagingException, ParseException {
        Calendar cal = Calendar.getInstance();
        System.out.println(cal.getTime() + "\r\n");
        
        Conexao con = new Conexao("base");
        ResultSet rs = null;
        List<Transacao> tr = new ArrayList<>();
        
        //realizando a consulta de transações pendentes
        Statement st = con.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        st.execute( "SELECT DISTINCT\n" +
                    "       att.mon$server_pid                                   as serverpid,\n" +
                    "       tr.mon$timestamp                                     as dthora,\n" +
                    "       cast(att.mon$remote_address as varchar(15))          as ip,\n" +
                    "       att.mon$user                                         as \"user\",\n" +
                    "       io.mon$page_fetches                                  as pagefetches\n" +
                    "FROM\n" +
                    "    mon$transactions    tr\n" +
                    "left join\n" +
                    "    mon$attachments     att     on (att.mon$attachment_id = tr.mon$attachment_id)\n" +
                    "left join\n" +
                    "    mon$io_stats        io      on (tr.mon$stat_id = io.mon$stat_id)\n" +
                    "where\n" +
                    "    tr.mon$lock_timeout = -1 and\n" +
                    "    tr.mon$state = 1 and\n" +
                    "    not att.mon$remote_pid is null and\n" +
                    "    io.mon$page_fetches > 1000000 and\n" +
                    "    datediff(minute from tr.mon$timestamp to current_timestamp) > 30\n" +
                    "order by\n" +
                    "    2;");
        
        rs = st.getResultSet();
        
        //carregando lista com dados obtidos da consulta
        Transacao trTemp;
        while(rs.next()){
            trTemp = new Transacao(rs.getString("serverpid"),
                                   rs.getString("dthora"),
                                   rs.getString("ip"),
                                   rs.getString("user"),
                                   rs.getString("pagefetches"));
            tr.add(trTemp);
        }
        
        //só envia e-mail se forem identificados transações pendentes
        if(tr.size() > 0){
            Email email = new Email("email_dest@destinatario.com.br");
            String msg = "Segue relação de transações pendetes: <br><br>" +
                         "<table style='border: solid 1px; border-collapse: collapse;'>\n" +
                         "<tr>\n" +
                         "<th style='border: solid 1px;'>PID</th>\n" +
                         "<th style='border: solid 1px;'>DATA E HORA</th>\n" +
                         "<th style='border: solid 1px;'>ENDEREÇO IP</th>\n" +
                         "<th style='border: solid 1px;'>USUÁRIO</th>\n" +
                         "<th style='border: solid 1px;'>PAGE FETCHES</th>\n" +
                         "</tr>\n";
            
            String pid;
            String dtHora;
            String ip;
            String user;
            String pageFetche;
            
            for(int i = 0; i < tr.size(); i++){
                pid = "<tr><td style='border: solid 1px'>" + tr.get(i).getServerPid().trim() + "</td>\n";
                dtHora = "<td style='border: solid 1px'>" + tr.get(i).getDtHora().trim() + "</td>\n";
                ip = "<td style='border: solid 1px'>" + tr.get(i).getIp().trim() + "</td>\n";
                user = "<td style='border: solid 1px'>" + tr.get(i).getUser().trim() + "</td>\n";
                pageFetche = "<td style='border: solid 1px'>" + tr.get(i).getPageFetches().trim() + "</td></tr>\n";
                msg += pid.concat(dtHora).concat(ip).concat(user).concat(pageFetche);    
            }
            msg += "</table>";
            
            //enviando email
            email.setCorpoEmail(msg);  
            email.enviarEmail();     
        }
        
        //fechando conexões
        con.con.close();
        st.close();
        rs.close();
        
        //chamando garbage colector
        System.gc();
    }   
}

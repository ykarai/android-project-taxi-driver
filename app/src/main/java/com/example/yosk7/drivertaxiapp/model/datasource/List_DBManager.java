//package com.example.yosk7.drivertaxiapp.model.datasource;
//
//import android.content.ContentValues;
//import android.location.Location;
//
//import com.androidproject.ya.clientapp.model.backend.Backend;
//import com.androidproject.ya.clientapp.model.entities.Client;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.androidproject.ya.clientapp.model.backend.Const.ContentValuesToCourse;
////import static com.androidproject.ya.clientapp.model.datasource.DS.clients;
//
//public class List_DBManager implements Backend {
//
//
//    static List<Client> clients;
//
//    static {
//        clients = new ArrayList<Client>();
//    }
//
//    //    public static List<Client> clients = new ArrayList<Client>();
//    @Override
//    public Long addClient(ContentValues values, Location a, Location locationA, Utils.Action<Long> action) {
//
//        Client client = ContentValuesToCourse(values);
//        clients.add(client);
//        return client.getId();
//
//
//    }
//
//    @Override
//    public boolean removeClient(Long id) {
//        Client courseToRemove = null;
//        for (Client item : clients)
//            if (item.getId() == id) {
//                courseToRemove = item;
//                break;
//            }
//        return clients.remove(courseToRemove);
//    }
//
//    @Override
//    public boolean updateClient(Long id, ContentValues values) {
//        Client client = ContentValuesToCourse(values);
//        //client.setName(name);  // to check
//        for (int i = 0; i < clients.size(); i++)
//            if (clients.get(i).getId() == id) {
//                clients.set(i, client);
//                return true;
//            }
//        return false;
//    }
//
//    @Override
//    public List<Client> getClients() {
//        return clients;
//    }
//}

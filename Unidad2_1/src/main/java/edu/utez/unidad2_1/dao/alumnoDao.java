package edu.utez.unidad2_1.dao;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import edu.utez.unidad2_1.models.Alumno;
import edu.utez.unidad2_1.models.Materia;

import java.util.ArrayList;
import java.util.Scanner;

public class alumnoDao {
    static Scanner leer = new Scanner(System.in);
    static ObjectContainer db = getConnection();
    public static ObjectSet<Alumno> consultaGen(){
        ObjectContainer db = getConnection();
        try{
            ObjectSet<Alumno> result = db.query(new Predicate<Alumno>() {
                public boolean match(Alumno o) {
                    return true;
                }
            });
            while (result.hasNext())
                System.out.println(result.next());
            return result;
        }catch (Exception e){
            System.out.println(e);
            db.rollback();
            return null;
        }
    }

    public boolean insert(Alumno alumno) {
        ObjectContainer db = getConnection();
        try {
            int i = getNewId(db);
            Alumno temp = new Alumno(i, alumno.getNombres(), alumno.getApellidos(), alumno.getEdad(), alumno.getMaterias());
            db.store(temp);
            db.commit();
            System.out.println("very god");
            return true;
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e);
            db.rollback();
            return false;
        }
    }

    public boolean update(Alumno alumno){
        ObjectContainer db = getConnection();
        try {
            ObjectSet resultado = db.queryByExample(new Alumno(alumno.getId(),null, null,0,new ArrayList<>()));
            if (resultado.hasNext()) {//If porq solo es
                Alumno temp = (Alumno) resultado.next();//
                temp.setNombres(alumno.getNombres());
                temp.setApellidos(alumno.getApellidos());
                temp.setEdad(alumno.getEdad());
                db.store(temp);
                db.commit();
                return true;
            } else {
                System.out.println("no existe");
                return false;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public boolean delete(Alumno alumno){
        ObjectContainer db = getConnection();
        try{
            ObjectSet result = db.queryByExample(new Alumno(alumno.getId(), alumno.getNombres(), alumno.getApellidos(), alumno.getEdad(), alumno.getMaterias()));
            Alumno found = (Alumno) result.next();
            System.out.println(found);
            db.delete(found);
            db.commit();
            return true;
        }catch (Exception e){
            db.rollback();
            return false;
        }
    }

    public static int getNewId(ObjectContainer db){
        ObjectSet<Alumno> result = db.query(new Predicate<Alumno>() {public boolean match(Alumno o) {return true;}});
        int d = 0, c=0;
        if (result == null){
            return 1;
        }
        while (result.hasNext()) {
            d = result.next().getId();
            if (d > c){c=d;}
        }
        c++;
        return c;
    }

    public static ObjectContainer getConnection(){
        try{
            ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "base.db4o");
            return db;
        }catch (Exception a){
            if (db== null){
                ObjectContainer dbNueva = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "base.db4o");
                return dbNueva;
            }else{
                db.close();
                ObjectContainer dbNueva = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "base.db4o");
                return dbNueva;
            }
        }
    }
}

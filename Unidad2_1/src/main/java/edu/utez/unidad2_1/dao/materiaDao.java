package edu.utez.unidad2_1.dao;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import edu.utez.unidad2_1.models.Materia;

import java.util.Scanner;

public class materiaDao {
    static Scanner leer = new Scanner(System.in);
    static ObjectContainer dbBasica = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "base.db4o");

    public static ObjectSet<Materia> consultaGen(){
        ObjectContainer db = getConnection();
        try{
            ObjectSet<Materia> result = db.query(new Predicate<Materia>() {
                public boolean match(Materia o) {
                    return true;
                }
            });

            return result;
        }catch (Exception e){
            System.out.println(e);
            db.rollback();

            return null;
        }

    }
    public boolean insert(Materia materia) {
        ObjectContainer db = getConnection();
        try {
            int i = getNewId(db);
            System.out.println(materia.getNombre()+" "+i);
            Materia mat = new Materia(i, materia.getNombre());
            db.store(mat);
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
    public boolean update(Materia materia){
        ObjectContainer db = getConnection();
        try {
            System.out.println("clave "+materia.getClave());
            ObjectSet resultado = db.queryByExample(new Materia(materia.getClave(), null));
            if (resultado.hasNext()) {//If porq solo es
                Materia temp = (Materia) resultado.next();//
                temp.setNombre(materia.getNombre());
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

    public boolean delete(Materia materia){
        ObjectContainer db = getConnection();
        try{
            ObjectSet result = db.queryByExample(
                    new Materia(materia.getClave(), materia.getNombre()));
            Materia found = (Materia)result.next();
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
        ObjectSet<Materia> result = db.query(new Predicate<Materia>() {public boolean match(Materia o) {return true;}});
        int d = 0, c=0;
        if (result == null){
            return 1;
        }
        while (result.hasNext()) {
            d = result.next().getClave();
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
            dbBasica.close();
            ObjectContainer dbNueva = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "base.db4o");
            return dbNueva;
        }
    }
}

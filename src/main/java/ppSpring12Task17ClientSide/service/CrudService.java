package ppSpring12Task17ClientSide.service;

import java.util.List;

/**
 * @author AkiraRokudo on 08.03.2020 in one of sun day
 */
public interface CrudService<T,T1> /*extends CrudRepository<User,Long> пока не будем трогать)*/ {

    T addObject(T1 t);

    void editObject(T1 t);

    /**
     * @param id искомого юзера
     * @return null если дали null или несуществующий айдишник
     */
    T getObject(Long id);

    /**
     *
     * @return null если дали null или несуществующий айдишник
     */
    T getObjectByParameters(String paramName, String paramValue);

    /**
     * @param id если null - Ничего не удалит
     */
    void removeObject(Long id);

    void removeObject(T1 t);

    List<T> getAll();

}

package com.example.ioc.service;

import com.example.ioc.dao.IndexDao;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service("indexService")
public abstract class IndexService {

//    @Autowired
//    public IndexService(IndexDao indexDao) {
//        this.indexDao = indexDao;
//    }

    public void service() {
        System.out.println(getIndexDao().hashCode());
        System.out.println(this.hashCode());
//        indexDao.test();
    }

    @Lookup
    protected abstract IndexDao getIndexDao();

}

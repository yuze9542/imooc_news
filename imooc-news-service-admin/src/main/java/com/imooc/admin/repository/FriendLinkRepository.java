package com.imooc.admin.repository;

import com.imooc.pojo.mo.FriendLinkMO;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO,String> {

    public List<FriendLinkMO> getAllByIsDelete(Integer isDelete);
}

package cn.itcast.elasticsearch.mapper;

import cn.itcast.elasticsearch.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}

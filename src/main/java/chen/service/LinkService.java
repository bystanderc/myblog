package chen.service;

import chen.common.ServerResponse;
import chen.dto.LinkDto;
import chen.entity.Link;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/1
 */
public interface LinkService {
    /**
     * 获取所有链接
     * @return
     */
    List<Link> getLinks();


    /**
     * 删除链接
     * @param linkId
     * @return
     */
    ServerResponse deleteLink(Integer linkId);

    ServerResponse getlinkById(Integer linkId);

    ServerResponse updateLink(LinkDto link);

    ServerResponse saveLink(LinkDto linkDto);
}

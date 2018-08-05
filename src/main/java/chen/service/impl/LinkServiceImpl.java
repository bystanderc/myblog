package chen.service.impl;

import chen.common.ServerResponse;
import chen.dao.LinkDtoMapper;
import chen.dto.LinkDto;
import chen.entity.Link;
import chen.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/1
 */
@Service("linkService")
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkDtoMapper linkMapper;

    @Override
    public List<Link> getLinks() {
        return linkMapper.getLinks();
    }

    @Override
    public ServerResponse deleteLink(Integer linkId) {
        int resultCount = linkMapper.deleteByPrimaryKey(linkId);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("删除链接失败");
        }
        return ServerResponse.createBySuccessMessage("删除链接成功");
    }

    @Override
    public ServerResponse getlinkById(Integer linkId) {
        LinkDto linkDto = linkMapper.selectByPrimaryKey(linkId);
        if (linkDto == null) {
            return ServerResponse.createByErrorMessage("该链接不存在");
        }
        return ServerResponse.createBySuccess(linkDto);
    }

    @Override
    public ServerResponse updateLink(LinkDto link) {
        int resultCount = linkMapper.updateByPrimaryKeySelective(link);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("更新链接失败");
        }
        return ServerResponse.createBySuccessMessage("更新链接成功");
    }

    @Override
    public ServerResponse saveLink(LinkDto linkDto) {
        int resultCount = linkMapper.insertSelective(linkDto);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("添加链接失败");
        }
        return ServerResponse.createBySuccessMessage("添加链接成功");
    }
}

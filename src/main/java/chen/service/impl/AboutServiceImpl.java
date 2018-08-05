package chen.service.impl;

import chen.common.ServerResponse;
import chen.dao.ArticleDtoMapper;
import chen.dto.AboutDto;
import chen.dto.ArticleDto;
import chen.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bystander
 * @date 2018/8/1
 */
@Service("/aboutService")
public class AboutServiceImpl implements AboutService {

    @Autowired
    private ArticleDtoMapper articleMapper;

    @Override
    public AboutDto getAbout() {
        return articleMapper.getAbout();
    }

    @Override
    public ServerResponse<String> updateAbout(String content) {
        int resultCount = articleMapper.updateAbout(content);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("更新失败");
        }
        return ServerResponse.createBySuccess("更新成功");
    }
}

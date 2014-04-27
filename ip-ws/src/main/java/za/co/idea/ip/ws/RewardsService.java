package za.co.idea.ip.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import za.co.idea.ip.orm.bean.IpRewards;
import za.co.idea.ip.orm.bean.IpRewardsCat;
import za.co.idea.ip.orm.bean.IpRewardsStatus;
import za.co.idea.ip.orm.dao.IpRewardsCatDAO;
import za.co.idea.ip.orm.dao.IpRewardsDAO;
import za.co.idea.ip.orm.dao.IpRewardsGroupDAO;
import za.co.idea.ip.orm.dao.IpRewardsStatusDAO;
import za.co.idea.ip.orm.dao.IpUserDAO;
import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.RewardsMessage;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Path(value = "/rs")
public class RewardsService {
	private IpUserDAO ipUserDAO;
	private IpRewardsCatDAO ipRewardsCatDAO;
	private IpRewardsDAO ipRewardsDAO;
	private IpRewardsStatusDAO ipRewardsStatusDAO;
	private IpRewardsGroupDAO ipRewardsGroupDAO;

	@POST
	@Path("/reward/add")
	@Consumes("application/json")
	@Produces("application/json")
	private ResponseMessage createReward(RewardsMessage rewards) {
		try {
			IpRewards ipRewards = new IpRewards();
			ipRewards.setIpRewardsCat(ipRewardsCatDAO.findById(rewards.getrCatId()));
			ipRewards.setIpRewardsStatus(ipRewardsStatusDAO.findById(rewards.getrStatusId()));
			ipRewards.setRwBlob(rewards.getRwBlob());
			ipRewards.setRwCrtdDt(rewards.getRwCrtdDt());
			ipRewards.setRwDesc(rewards.getRwDesc());
			ipRewards.setRwExpiryDt(rewards.getRwExpiryDt());
			ipRewards.setRwFileName(rewards.getRwFileName());
			ipRewards.setRwFileType(rewards.getRwFileType());
			ipRewards.setRwHoverText(rewards.getRwHoverText());
			ipRewards.setRwId(rewards.getRwId());
			ipRewards.setRwLaunchDt(rewards.getRwLaunchDt());
			ipRewards.setRwStockCodeNum(rewards.getRwStockCodeNum());
			ipRewards.setRwTag(rewards.getRwTag());
			ipRewards.setRwTitle(rewards.getRwTitle());
			ipRewards.setRwValue(rewards.getRwValue());
			ResponseMessage message = new ResponseMessage();
			message.setStatusCode(0);
			message.setStatusDesc("Success");
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage message = new ResponseMessage();
			message.setStatusCode(1);
			message.setStatusDesc(e.getMessage());
			return message;
		}
	}

	@POST
	@Path("/reward/modify")
	@Consumes("application/json")
	@Produces("application/json")
	private ResponseMessage updateReward(RewardsMessage rewards) {
		try {
			IpRewards ipRewards = new IpRewards();
			ipRewards.setIpRewardsCat(ipRewardsCatDAO.findById(rewards.getrCatId()));
			ipRewards.setIpRewardsStatus(ipRewardsStatusDAO.findById(rewards.getrStatusId()));
			ipRewards.setRwBlob(rewards.getRwBlob());
			ipRewards.setRwCrtdDt(rewards.getRwCrtdDt());
			ipRewards.setRwDesc(rewards.getRwDesc());
			ipRewards.setRwExpiryDt(rewards.getRwExpiryDt());
			ipRewards.setRwFileName(rewards.getRwFileName());
			ipRewards.setRwFileType(rewards.getRwFileType());
			ipRewards.setRwHoverText(rewards.getRwHoverText());
			ipRewards.setRwId(rewards.getRwId());
			ipRewards.setRwLaunchDt(rewards.getRwLaunchDt());
			ipRewards.setRwStockCodeNum(rewards.getRwStockCodeNum());
			ipRewards.setRwTag(rewards.getRwTag());
			ipRewards.setRwTitle(rewards.getRwTitle());
			ipRewards.setRwValue(rewards.getRwValue());
			ResponseMessage message = new ResponseMessage();
			message.setStatusCode(0);
			message.setStatusDesc("Success");
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage message = new ResponseMessage();
			message.setStatusCode(1);
			message.setStatusDesc(e.getMessage());
			return message;
		}
	}

	@GET
	@Path("/rewards/list")
	@Produces("application/json")
	public <T extends RewardsMessage> List<T> listRewards() {
		List<T> ret = new ArrayList<T>();
		try {
			List rewardList = ipRewardsDAO.findAll();
			for (Object object : rewardList) {
				IpRewards ipRewards = (IpRewards) object;
				RewardsMessage rewards = new RewardsMessage();
				rewards.setrCatId(ipRewards.getIpRewardsCat().getRcId());
				rewards.setrStatusId(ipRewards.getIpRewardsStatus().getRsId());
				rewards.setRwBlob(ipRewards.getRwBlob());
				rewards.setRwCrtdDt(ipRewards.getRwCrtdDt());
				rewards.setRwDesc(ipRewards.getRwDesc());
				rewards.setRwExpiryDt(ipRewards.getRwExpiryDt());
				rewards.setRwFileName(ipRewards.getRwFileName());
				rewards.setRwFileType(ipRewards.getRwFileType());
				rewards.setRwHoverText(ipRewards.getRwHoverText());
				rewards.setRwId(ipRewards.getRwId());
				rewards.setRwLaunchDt(ipRewards.getRwLaunchDt());
				rewards.setRwStockCodeNum(ipRewards.getRwStockCodeNum());
				rewards.setRwTag(ipRewards.getRwTag());
				rewards.setRwTitle(ipRewards.getRwTitle());
				rewards.setRwValue(ipRewards.getRwValue());
				ret.add((T) rewards);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/rewards/get/{id}")
	@Produces("application/json")
	public RewardsMessage getRewardsById(@PathParam("id") Long id) {
		RewardsMessage rewards = new RewardsMessage();
		try {
			IpRewards ipRewards = ipRewardsDAO.findById(id);
			rewards.setrCatId(ipRewards.getIpRewardsCat().getRcId());
			rewards.setrStatusId(ipRewards.getIpRewardsStatus().getRsId());
			rewards.setRwBlob(ipRewards.getRwBlob());
			rewards.setRwCrtdDt(ipRewards.getRwCrtdDt());
			rewards.setRwDesc(ipRewards.getRwDesc());
			rewards.setRwExpiryDt(ipRewards.getRwExpiryDt());
			rewards.setRwFileName(ipRewards.getRwFileName());
			rewards.setRwFileType(ipRewards.getRwFileType());
			rewards.setRwHoverText(ipRewards.getRwHoverText());
			rewards.setRwId(ipRewards.getRwId());
			rewards.setRwLaunchDt(ipRewards.getRwLaunchDt());
			rewards.setRwStockCodeNum(ipRewards.getRwStockCodeNum());
			rewards.setRwTag(ipRewards.getRwTag());
			rewards.setRwTitle(ipRewards.getRwTitle());
			rewards.setRwValue(ipRewards.getRwValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rewards;
	}

	@GET
	@Path("/rewards/list/{id}")
	@Produces("application/json")
	public <T extends RewardsMessage> List<T> listRewardsByStatus(@PathParam("id") Integer id) {
		List<T> ret = new ArrayList<T>();
		try {
			List rewardList = ipRewardsDAO.findByStatusId(id);
			for (Object object : rewardList) {
				IpRewards ipRewards = (IpRewards) object;
				RewardsMessage rewards = new RewardsMessage();
				rewards.setrCatId(ipRewards.getIpRewardsCat().getRcId());
				rewards.setrStatusId(ipRewards.getIpRewardsStatus().getRsId());
				rewards.setRwBlob(ipRewards.getRwBlob());
				rewards.setRwCrtdDt(ipRewards.getRwCrtdDt());
				rewards.setRwDesc(ipRewards.getRwDesc());
				rewards.setRwExpiryDt(ipRewards.getRwExpiryDt());
				rewards.setRwFileName(ipRewards.getRwFileName());
				rewards.setRwFileType(ipRewards.getRwFileType());
				rewards.setRwHoverText(ipRewards.getRwHoverText());
				rewards.setRwId(ipRewards.getRwId());
				rewards.setRwLaunchDt(ipRewards.getRwLaunchDt());
				rewards.setRwStockCodeNum(ipRewards.getRwStockCodeNum());
				rewards.setRwTag(ipRewards.getRwTag());
				rewards.setRwTitle(ipRewards.getRwTitle());
				rewards.setRwValue(ipRewards.getRwValue());
				ret.add((T) rewards);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/rewards/list/{id}")
	@Produces("application/json")
	public <T extends RewardsMessage> List<T> listRewardsByUser(@PathParam("id") Long id) {
		List<T> ret = new ArrayList<T>();
		try {
			List rewardList = ipRewardsDAO.findByUserId(id);
			for (Object object : rewardList) {
				IpRewards ipRewards = (IpRewards) object;
				RewardsMessage rewards = new RewardsMessage();
				rewards.setrCatId(ipRewards.getIpRewardsCat().getRcId());
				rewards.setrStatusId(ipRewards.getIpRewardsStatus().getRsId());
				rewards.setRwBlob(ipRewards.getRwBlob());
				rewards.setRwCrtdDt(ipRewards.getRwCrtdDt());
				rewards.setRwDesc(ipRewards.getRwDesc());
				rewards.setRwExpiryDt(ipRewards.getRwExpiryDt());
				rewards.setRwFileName(ipRewards.getRwFileName());
				rewards.setRwFileType(ipRewards.getRwFileType());
				rewards.setRwHoverText(ipRewards.getRwHoverText());
				rewards.setRwId(ipRewards.getRwId());
				rewards.setRwLaunchDt(ipRewards.getRwLaunchDt());
				rewards.setRwStockCodeNum(ipRewards.getRwStockCodeNum());
				rewards.setRwTag(ipRewards.getRwTag());
				rewards.setRwTitle(ipRewards.getRwTitle());
				rewards.setRwValue(ipRewards.getRwValue());
				ret.add((T) rewards);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/rewards/cat/list")
	@Produces("application/json")
	public <T extends MetaDataMessage> List<T> listRewardsCat() {
		List<T> ret = new ArrayList<T>();
		try {
			List rewardsCats = ipRewardsCatDAO.findAll();
			for (Object object : rewardsCats) {
				IpRewardsCat cat = (IpRewardsCat) object;
				MetaDataMessage message = new MetaDataMessage();
				message.setId(cat.getRcId());
				message.setDesc(cat.getRcDesc());
				ret.add((T) message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/rewards/status/list")
	@Produces("application/json")
	public <T extends MetaDataMessage> List<T> listRewardsStatus() {
		List<T> ret = new ArrayList<T>();
		try {
			List rewardsStatus = ipRewardsStatusDAO.findAll();
			for (Object object : rewardsStatus) {
				IpRewardsStatus status = (IpRewardsStatus) object;
				MetaDataMessage message = new MetaDataMessage();
				message.setId(status.getRsId());
				message.setDesc(status.getRsDesc());
				ret.add((T) message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/rewards/cat/get/{id}")
	@Produces("application/json")
	public MetaDataMessage getRewardsCatById(@PathParam("id") Integer id) {
		MetaDataMessage message = new MetaDataMessage();
		try {
			IpRewardsCat cat = ipRewardsCatDAO.findById(id);
			message.setId(cat.getRcId());
			message.setDesc(cat.getRcDesc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	@GET
	@Path("/rewards/status/get/{id}")
	@Produces("application/json")
	public MetaDataMessage getRewardsStatusById(@PathParam("id") Integer id) {
		MetaDataMessage message = new MetaDataMessage();
		try {
			IpRewardsStatus status = ipRewardsStatusDAO.findById(id);
			message.setId(status.getRsId());
			message.setDesc(status.getRsDesc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	public IpUserDAO getIpUserDAO() {
		return ipUserDAO;
	}

	public void setIpUserDAO(IpUserDAO ipUserDAO) {
		this.ipUserDAO = ipUserDAO;
	}

	public IpRewardsCatDAO getIpRewardsCatDAO() {
		return ipRewardsCatDAO;
	}

	public void setIpRewardsCatDAO(IpRewardsCatDAO ipRewardsCatDAO) {
		this.ipRewardsCatDAO = ipRewardsCatDAO;
	}

	public IpRewardsDAO getIpRewardsDAO() {
		return ipRewardsDAO;
	}

	public void setIpRewardsDAO(IpRewardsDAO ipRewardsDAO) {
		this.ipRewardsDAO = ipRewardsDAO;
	}

	public IpRewardsStatusDAO getIpRewardsStatusDAO() {
		return ipRewardsStatusDAO;
	}

	public void setIpRewardsStatusDAO(IpRewardsStatusDAO ipRewardsStatusDAO) {
		this.ipRewardsStatusDAO = ipRewardsStatusDAO;
	}

	public IpRewardsGroupDAO getIpRewardsGroupDAO() {
		return ipRewardsGroupDAO;
	}

	public void setIpRewardsGroupDAO(IpRewardsGroupDAO ipRewardsGroupDAO) {
		this.ipRewardsGroupDAO = ipRewardsGroupDAO;
	}

}

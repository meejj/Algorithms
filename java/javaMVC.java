
 //list
	@RequestMapping(value = "/schdlView/ESlist.do") - list 리스트를 보여준다는 뜻
	public String schdlViewlistESAction(@ModelAttribute("schdlViewVO") schdlViewVO schdlViewVO, HttpServletRequest request, HttpServletResponse response) throws Exception { /뷰vo 객체 엑셀할때 필요/vo객체필요, 요청과 반환 사용 

		if (!getSysInfo(request)) 시스템정보 요청해서 값이 없을때(아닐때) 에러값 반환
			return "/error.do";
		if (!getTopMenu(request)) 상단메뉴정보 요청했을때 값이 없을때 에러값 반환
			return "/error.do";
		if (!getLeftMenu(request)) 왼쪽메뉴 요청했을때 값이 아니면 에러값 반환
			return "/error.do";

		HttpSession session = request.getSession(); 세션정보를 뷰에 요청해서 C로 가지고 와서 세션에 저장
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo"); 세션정보중에 sysinfo 정보를 sysinfo에 저장
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer");
		UsersVO userInfo = null; 유저vo userinfo 눌값 저장
		List authList = null; authList 권한리스트값 눌로 저장
		List sysschdlList = null; sysschdlist 값 눌값으로 저장
		String chks = "초등"; 문자열로 chks  초등 저장
		boolean bIsMgtAuth = false; 관리자권한에 false 설정
		
		if(userContainer != null) { 세션정보에서 가지고온 유저컨테이너가 null값이 아니면
			userInfo = userContainer.getUsers(); userInfo에 회원정보저장 유저컨테이너에 겟유저스를 저장한다는 뜻
			authList = loginService.getUsrAuthList(userInfo.getUsrID()); 유저인포에 겟유저아이디에 해당하는 권한리스트를 모델에서 가지고와서 권한리스트에 저장
			String neisCD = sysInfo.getNeisCD(); 시스템정보에 나이스cd를 나이스cd에 문자열로 저장
			if (userInfo != null) { 만약 사용자인포가 눌값이 아니면
				if (authList != null) authlist값이 눌값이 아니면
					userContainer.setAuths(authList); 사용자 권한정보 다 가져와서 셋어스에 권한정보를 리스트에 담아서 setAuth에 담음 환경설정
			+
				if (userContainer.hasSchdlViewListMgtAuth(neisCD)) { 나이스씨디는 키값이고 세팅한거를 비교해줌 / neisCD 로 권한 비교
					bIsMgtAuth = true; 관리자권한값에 해당하면 true 설정 트루설정bis값에 트루값 설정
				}
			}
		}
		request.setAttribute("bIsMgtAuth", bIsMgtAuth); bismgtauth 값에 false 설정
		

		try {
			sysschdlList = schdlViewService.getSchdlViewList(chks, sysInfo.getSysID()); chks, getsysid에 해당하는 뷰리스트를 모델에서 가지고 와서 sysschdlist에 저장저장

			} catch (SQLException e) {
			LOG.error("schdlViewController SQLException : schdlViewlistAction");
			String strErr = "일정보기를 실패하였습니다.\\n\\n시스템 관리자에게 문의하세요.";
			AlertMessage.htmlPrint(response, strErr);
			return null;
		}

		request.setAttribute("chks", chks); chks는 초딩 chks 값 뷰에 보냄
		request.setAttribute("sysschdlList", sysschdlList); list 값 뷰에 보냄
		
		String excel = Utils.nvl(schdlViewVO.getExcel(), ""); 브이오객체에 엑셀 값을 엑셀에 문자열로 저장
		if (excel.equals("excel")) { 엑셀값이 맞다면
			return "/addon/schdlView/excel"; 이 url로 반환
		}
		
		request.setAttribute("contents", "/addon/schdlView/ESlist.jsp"); 엑셀값이 안되다면 eslist.jsp url로 이동

		return "/layouts/" + sysInfo.getSysID() + "Layout";
	}
//list
	@RequestMapping(value = "/schdlView/MSlist.do") list 형식 보여줌 url 매핑
	public String schdlViewlistMSAction(@ModelAttribute("schdlViewVO") schdlViewVO schdlViewVO, HttpServletRequest request, HttpServletResponse response) throws Exception { vo 요청 반환 다씀
		
		if (!getSysInfo(request)) 시스템정보 요청한 값이 없을때 에러페이지 반환
			return "/error.do";
		if (!getTopMenu(request)) 상단메뉴 요청한 값이 없을때 에러페이지 반환
			return "/error.do";
		if (!getLeftMenu(request)) 왼쪽메뉴 요청한 값이 없을때 에러페이지 반환
			return "/error.do";
		
		HttpSession session = request.getSession(); 세션 요청값을 세션에 저장(뷰에서 요청해서 가지고옴)
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo"); 세션 요청 값중 sysInfo값을 sysinfo에 저장
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer"); 세션값중 유저컨테이너값을 유저컨테이너에 저장
		UsersVO userInfo = null; userVo에 유저인포에 눌값 저장 /목적이 권한비교를 위해서 사용 null값 이용한것들
		List authList = null; 리스트 authList에 눌값 저장
		List sysschdlList = null; sysschdList에 눌값 저장
		String chks = "중등"; chks에 문자열로 중등값 저장
		
		boolean bIsMgtAuth = false; bIsMgt에 기본 false 값 저장
		
		if(userContainer != null) { 유저컨테이너값이 눌값이 아니면
			userInfo = userContainer.getUsers(); 유저인포에 유저컨테이너값에 있는 회원정보값을 저장(뷰 세션에서 가지고온)
			authList = loginService.getUsrAuthList(userInfo.getUsrID()); getusrid 에 해당하는 건한리스트를 모델에서 가지고와서 autlist에 저장
			String neisCD = sysInfo.getNeisCD(); 문자열로 sysinfo나이스아이디를 나이스 아이디에 저장
			if (userInfo != null) { 유저인포값이 눌값이 아니면
				if (authList != null) authlist 값이 눌값이 아니면 / 권한이 있는지 없는지 if문으로
					userContainer.setAuths(authList); 사용자 권한정보 다 가져와서 셋어스에 권한정보 다 넣어줌 c 가  /세팅 환경설정 /권한세팅
				if (userContainer.hasSchdlViewListMgtAuth(neisCD)) {  나이스씨디는 키값이고 세팅한거를 비교해줌 나이스씨디값을 가지고 권한이랑 비교
					bIsMgtAuth = true; 권한이 있으면 트루값 설정
				}
			}
		}
		
		request.setAttribute("bIsMgtAuth", bIsMgtAuth); 권한이 없으면 false / 눌값이면 bismgtauth 값이 false
		
		try {
			sysschdlList = schdlViewService.getSchdlViewList(chks, sysInfo.getSysID()); 모델에서 조회할때 조건이 chks sysid 이용해서 조회 
		} catch (SQLException e) {
			LOG.error("schdlViewController SQLException : schdlViewlistAction");
			String strErr = "일정보기를 실패하였습니다.\\n\\n시스템 관리자에게 문의하세요.";
			AlertMessage.htmlPrint(response, strErr);
			return null;
		}
		
		request.setAttribute("chks", chks);
		request.setAttribute("sysschdlList", sysschdlList); chks와 list값을 뷰로 보냄
		
		String excel = Utils.nvl(schdlViewVO.getExcel(), ""); 문자열로 엑셀값 저장
		if (excel.equals("excel")) { 엑셀값이 있으면
			return "/addon/schdlView/excel"; url로 반환
		}
		
		request.setAttribute("contents", "/addon/schdlView/MSlist.jsp"); 엑셀값이 없으면 list.jsp 페이지로 감
		
		return "/layouts/" + sysInfo.getSysID() + "Layout";
	}
	
	//list
	@RequestMapping(value = "/schdlView/HSlist.do") url 값 매핑
	public String schdlViewlistHSAction(@ModelAttribute("schdlViewVO") schdlViewVO schdlViewVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (!getSysInfo(request))
			return "/error.do";
		if (!getTopMenu(request))
			return "/error.do";
		if (!getLeftMenu(request))
			return "/error.do";
		
		HttpSession session = request.getSession(); 세션값 요청한것 세션에 저장
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo"); 세션값중 sysinfo값 sysinfo에 저장
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer"); 세션값중 유저컨테이너값 유저컨테이너에 저장
		UsersVO userInfo = null; 유저vo에 유저인포를 눌값으로 저장
		List authList = null; 리스트 형식으로 authlist 값 눌값으로 저장
		List sysschdlList = null; 리스트형식으로 sysschdlist값 눌값으로 저장
		String chks = "고등"; 문자열 형식으로 chks에 고등값 저장
		
		boolean bIsMgtAuth = false; bismgtauth에 폴스값 저장
		
		if(userContainer != null) { 유저컨테이너값이 눌값이 아니면
			userInfo = userContainer.getUsers(); 유저컨네이너값에 회원정보인 겟유저를 유저인포에 저장
			authList = loginService.getUsrAuthList(userInfo.getUsrID()); 모델에서 가지고온 권한리스트를 authlist에 저장
			String neisCD = sysInfo.getNeisCD(); 문자열로 sysinfo값에 나이스cd값을 neiscd에 저장
			if (userInfo != null) { 유저인포값이 눌값이 아니면
				if (authList != null) authlist 값이 눌값이 아니면
					userContainer.setAuths(authList); authlist값을 유저컨테이너 setauth에 저장 /세팅 환경설정******************************
				if (userContainer.hasSchdlViewListMgtAuth(neisCD)) { 세팅한것을 비교*************************************
					bIsMgtAuth = true; bismgtaut 값 트루
				}
			}
		}
		request.setAttribute("bIsMgtAuth", bIsMgtAuth); 기본값 false 반환 뷰로
		
		try {
			sysschdlList = schdlViewService.getSchdlViewList(chks, sysInfo.getSysID()); 모델에서 가지고온 정보를 sysschdlist에 저장**************************
		} catch (SQLException e) {
			LOG.error("schdlViewController SQLException : schdlViewlistAction");
			String strErr = "일정보기를 실패하였습니다.\\n\\n시스템 관리자에게 문의하세요.";
			AlertMessage.htmlPrint(response, strErr);
			return null;
		}
		
		request.setAttribute("chks", chks);
		request.setAttribute("sysschdlList", sysschdlList); chks랑 sysschd를 뷰로 보여줌
		
		String excel = Utils.nvl(schdlViewVO.getExcel(), ""); 엑셀을 문자열로 저장
		if (excel.equals("excel")) { 엑셀값이 있으면
			return "/addon/schdlView/excel"; 이 url 반환
		}
		
		request.setAttribute("contents", "/addon/schdlView/HSlist.jsp"); 없으면 뷰로 내용 반환
		
		return "/layouts/" + sysInfo.getSysID() + "Layout";
	}
	
	@RequestMapping(value = "/schdlView/view.do") 이 url 매핑 /view 하나만 보려고 사용 /리스트는 전체고 뷰는 내가 원하는거 보기위해서 사용
	public String schdlViewlistVIEWAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (!getSysInfo(request)) 시스정보 요청한 값이 없을때 url 반환
			return "/error.do";
		if (!getTopMenu(request)) 상단메뉴 요청한 값이 없을때 url 반환
			return "/error.do";
		if (!getLeftMenu(request)) 왼쪽메뉴 요청한 값이 없을때 url 반환
			return "/error.do";
		
		HttpSession session = request.getSession(); 뷰로에서 세션정보 요청해서 세션에 저장
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo"); 세션정보중에 sysinfo 값을 sysinfo에 저장
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer"); 세션정보중에 usercontainer값을 usercontainer에저장
		UsersVO userInfo = null; userinfo 에 눌값 저장
		List authList = null; 리스트 auth리스트에 눌값 저장
		List schdlList = null; schd리스트에 눌값 저장
		String seq = request.getParameter("seq"); 순서번호를 문자열로 뷰에서 가지고와서 seq에 저장/ seq사용이유가 전체중에 일부분 보기위해서 사용 /뷰라는 
		String chks = request.getParameter("chks"); chks를 뷰에서 가지고와서 문자열로 chks에 저장
		request.setAttribute("seq", seq); 
		request.setAttribute("chks", chks); 순서번호와 chks를 뷰로 보냄
		
		boolean bIsMgtAuth = false;  / 관리자권한  bismg값에 false값 저장
		boolean bIsAuth = false; / 사용자권한 bis값에 false값 저장
		
		if(userContainer != null) { 유저컨테이너값이 눌값이 아니면
			userInfo = userContainer.getUsers(); 유저컨테이너에 회원정보를 userinfo 에저장
			authList = loginService.getUsrAuthList(userInfo.getUsrID()); 모델에서 user권환리스트를 authlist 에 저장
			String neisCD = sysInfo.getNeisCD(); sysinfo에 나이스cd값을 문자열로 나이스cd에 저장
			if (userInfo != null) { 유저인포값이 눌값이 아닐때
				if (authList != null) authlist에 값이 눌값이 아닐때 
					userContainer.setAuths(authList); 권한리스트를 유저컨테이너에 setauth에 저장 세팅환경설정 ***************
				
				if (userContainer.hasSchdlViewListMgtAuth(neisCD)) { 나이스시디에 키값을 이용해서 비교
					bIsMgtAuth = true; 맞으면 true / 관리자권한 맞으면 true
				}
				if (userContainer.hasSchdlViewListSupportMgtAuth(neisCD)) { 나이스시디 키값 이용해서 비교 / 사용자권한 관리자권한 따로 비교
					bIsAuth = true; 맞으면 true/사용자권한 맞으면 true
				}
			}
		}
		request.setAttribute("bIsMgtAuth", bIsMgtAuth); 유저컨테이너값이 눌값이면 bismgtauth에 false값 반환
		request.setAttribute("bIsAuth", bIsAuth);  유저컨테이너값이 눌값이면 bisauth에 false값 반환
		
		try {
			schdlList = schdlViewService.getSchdlView(Integer.parseInt(seq)); 모델에서 가지고온 순서번호를 schdlist에 저장 / 겟스케줄뷰 = 1번박미정의정보를 리스트형식으로 담아라 
		} catch (SQLException e) {
			LOG.error("schdlViewController SQLException : schdlViewAction");
			String strErr = "일정보기를 실패하였습니다.\\n\\n시스템 관리자에게 문의하세요.";
			AlertMessage.htmlPrint(response, strErr);
			return null;
		}
		
		request.setAttribute("schdlList", schdlList);  뷰로 보내기 1번박미정정보를
		request.setAttribute("contents", "/addon/schdlView/view.jsp"); 
		
		return "/layouts/" + sysInfo.getSysID() + "Layout";
	}
	
	@RequestMapping(value = "/schdlView/Fmodify.do") fmodify 수정전에 그릇들이랑 어떤게 담겨잇는지 세팅 / url 매핑
	public String schdlViewlistFmodAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (!getSysInfo(request))
			return "/error.do";
		if (!getTopMenu(request))
			return "/error.do";
		if (!getLeftMenu(request))
			return "/error.do";
		
		HttpSession session = request.getSession();
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo");
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer");
		UsersVO userInfo = null;
		List authList = null;
		List schdlList = null;
		String seq = request.getParameter("seq"); V에서 C로 순서번호 가지고와서 문자열 seq 저장****************************
		request.setAttribute("seq", seq); 순서번호 뷰로 보냄
		String chks = request.getParameter("chks"); chks V에서 C로 가지고 와서 문자열로 chks 저장
		request.setAttribute("chks", chks); chks 뷰로 보냄
		
		try {
			schdlList = schdlViewService.getSchdlView(Integer.parseInt(seq)); 모델에서 순서번호 가지고와서 schdlist에 저장
		} catch (SQLException e) {
			LOG.error("schdlViewController SQLException : schdlViewlistFmodAction");
			String strErr = "일정보기를 실패하였습니다.\\n\\n시스템 관리자에게 문의하세요.";
			AlertMessage.htmlPrint(response, strErr);
			return null;
		}
		
		request.setAttribute("schdlList", schdlList);
		request.setAttribute("contents", "/addon/schdlView/Fmodify.jsp"); schdlist 와 내용 뷰로 보냄
		
		return "/layouts/" + sysInfo.getSysID() + "Layout";
	} 수정을 할수있게 보여주는것 = fmodify / 수정입력가능 / 뷰는 수정입력불가능
	
	@RequestMapping(value = "/schdlView/modify.do")
	public String schdlViewlistmodAction(@ModelAttribute("schdlViewVO") schdlViewVO schdlViewVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (!getSysInfo(request))
			return "/error.do";
		if (!getTopMenu(request))
			return "/error.do";
		if (!getLeftMenu(request))
			return "/error.do";
		
		HttpSession session = request.getSession();
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo");
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer");
		UsersVO userInfo = null;
		if(userContainer != null) {
			userInfo = userContainer.getUsers(); 유저컨테이너에회원정보를 userinfo에 저장
			String neisCD = sysInfo.getNeisCD(); sysinfo에 나이스cd를 문자열로 나이스cd에 저장
		}else {
			return "forward:/login.do"; 눌값이면 권한이 없는것으로 로그인페이지로 가라는 뜻
		}
		
		try {
			schdlViewService.modSchdlView(schdlViewVO); mod=update viewvo 데이터를 그대로 넣어줌 modschdview에 넣어줌
			
			schdlViewVO.setSeq(schdlViewService.getSeq("KL_SCHDL_VIEW_LIST_LOG_SEQ1")); 로그 이력 남김 가지고온 순서번호 vo객체에 넣어줌 / c가 직접 설정한 순서번호를 뷰vo객체네 넣음
			schdlViewVO.setRegIP(super.accessIP(request)); setRegIP에 가지고온 ip넣어줌
			schdlViewVO.setUsrID(userInfo.getUsrID());
			schdlViewVO.setUseType("수정");
			
			schdlViewService.insSchdlViewListLog(schdlViewVO); 브이오객체에 메모한것을 모델에 기록
			
			request.setAttribute("resultType", "MY"); 뷰에 결과값 my 보냄
		} catch (SQLException e) {
			request.setAttribute("resultType", "MN"); 예외발생하면 뷰에 결과값 mn 보냄
			LOG.error("schdlViewController SQLException : schdlViewlistmodAction");
			String strErr = "일정보기를 실패하였습니다.\\n\\n시스템 관리자에게 문의하세요.";
			AlertMessage.htmlPrint(response, strErr);
			return null;
		}
		
		return "/addon/schdlView/result"; 뷰로감
	}
	
	@RequestMapping(value = "/schdlView/delete.do")
	public String schdlViewlistdelAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (!getSysInfo(request))
			return "/error.do";
		if (!getTopMenu(request))
			return "/error.do";
		if (!getLeftMenu(request))
			return "/error.do";
		
		HttpSession session = request.getSession();
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo");
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer");
		UsersVO userInfo = null;
		List authList = null;
		List schdlList = null;
		String seq = request.getParameter("seq"); 뷰에서 가지고온 순서번호를 seq에 저장
		
		try {
			schdlViewService.delSchdlView(Integer.parseInt(seq)); 순서번호를 delschdview에 저장해서 모델에 보냄
			request.setAttribute("resultType", "DY"); 결과값이 맞으면 DY로
		} catch (SQLException e) {
			request.setAttribute("resultType", "DN");
			LOG.error("schdlViewController SQLException : schdlViewlistdelAction");
			String strErr = "일정보기를 실패하였습니다.\\n\\n시스템 관리자에게 문의하세요.";
			AlertMessage.htmlPrint(response, strErr);
			return null;
		}
		
		return "/addon/schdlView/result";
	}
	
	@RequestMapping(value = "/schdlView/FinsForm.do") 등록전단계 모델을 안거침
	public String schdlViewFInsAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (!getSysInfo(request))
			return "/error.do";
		if (!getTopMenu(request))
			return "/error.do";
		if (!getLeftMenu(request))
			return "/error.do";
		
		HttpSession session = request.getSession();
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo");
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer");
		UsersVO userInfo = null;
		List authList = null;
		String chks = request.getParameter("chks"); chks를 뷰로 가지고와서 문자열로 chks에 저장
		request.setAttribute("chks", chks); chks 값을 뷰로 보냄
		if (userContainer == null) { 유저컨테이너 값이 눌값이면
			return "forward:/login.do"; 이 로그인 페이지로 감
		}
		else {
			userInfo = userContainer.getUsers(); 유저인포에 유저컨테이너회원정보저장
		}
		
		request.setAttribute("contents", "/addon/schdlView/Finsert.jsp"); 유저컨테이너값이 눌값이 아니면 내용 뷰로 보냄
		
		return "/layouts/" + sysInfo.getSysID() + "Layout";
	}
	
	@RequestMapping(value = "/schdlView/insForm.do")
	public String schdlViewInsAction(@ModelAttribute("schdlViewVO") schdlViewVO schdlViewVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (!getSysInfo(request))
			return "/error.do";
		if (!getTopMenu(request))
			return "/error.do";
		if (!getLeftMenu(request))
			return "/error.do";
		
		HttpSession session = request.getSession();
		SysMgtVO sysInfo = (SysMgtVO) session.getAttribute("sysInfo");
		UserContainer userContainer = (UserContainer) session.getAttribute("userContainer");
		UsersVO userInfo = null;
		List authList = null;
		if (userContainer == null) { 유저컨테이너값이 눌값이면
			return "forward:/login.do"; 이 url 반환
		}
		else {
			userInfo = userContainer.getUsers(); 아니면 유저컨테이너에 회원정보를 userinfo에 저장
		}
		
		try {
			schdlViewVO.setSeq(schdlViewService.getSeq("KL_SCHDL_VIEW_LIST_SEQ1")); 반번호 순서번호를 브이오객체에 메모 /컨트롤러가 직접 번호랑 id를 직접 결정해서 넣어줌 브이오객체에 고유정보를 넣어준다 = get
			schdlViewVO.setSysID(sysInfo.getSysID()); sysid를 메모
			schdlViewService.insSchdlViewList(schdlViewVO);insschdview리스트에 넣어줌 브이오통채로 viewvo객제 메모 / vo객체를 통째로 넣어준다.
			request.setAttribute("resultType", "IY"); 결과값을 iy로 뷰에 보냄
		} catch (SQLException e) {
			request.setAttribute("resultType", "IN");
			LOG.error("schdlViewController SQLException : schdlViewFInsAction");
			String strErr = "등록에 실패하였습니다.\\n\\n시스템 관리자에게 문의하세요.";
			AlertMessage.htmlPrint(response, strErr);
			return null;
		}
		
		return "/addon/schdlView/result";
	}

}

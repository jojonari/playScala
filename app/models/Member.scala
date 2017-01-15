package models

import java.util.{Date}
import javax.inject.{Inject, Singleton}

import anorm._
import anorm.SqlParser._
import play.api.db.{DBApi}

/**
  * Created by jojonari on 2017. 1. 15..
  */
@Singleton
class MemberService @Inject() (dBApi: DBApi) {
  private val db = dBApi.database("default")

  val basicMember = {
    get[Int]("member.mid") ~
    get[String]("member.userid") ~
    get[String]("member.password") ~
    get[String]("member.nickname") ~
    get[String]("member.email") ~
    get[Option[Date]]("member.regdate") map{
      case mid ~ userid ~ passowrd ~ nickname ~ email ~ regdate =>
        Member(mid, userid, passowrd, nickname, email, regdate)
    }
  }

  def getList = db.withConnection{implicit connection =>
    SQL("select * from member").as(basicMember *)
  }

  def insert(member: Member) = {
    db.withConnection{ implicit connection =>
      SQL(
        """
          insert into member (userid, password, nickname, email, regdate)
          values( {userid}, {password}, {nickname}, {email}, {regdate} )
        """
      ).on(
        'userid -> member.userid,
        'password -> member.password,
        'nickname -> member.nickName,
        'email -> member.email,
        'regdate -> member.regDate
      ).executeUpdate()
    }
  }
}


/** case class는 객체 인스턴스를 생성하기 위한 new가 필요하지 않다.*/
case class Member (mid:Int, userid:String, password:String, nickName:String, email:String, regDate:Option[Date])
//
//object Member {
//    def getList = members
//    var date =Option.apply(Calendar.getInstance().getTime)
//
//  var members = Set(
//    Member(1, "admin", "12345", "admin", "admin@google.com", date),
//    Member(2, "kim", "123456", "kim", "kim@google.com", date),
//    Member(3, "baek", "123457", "baek", "baek@google.com", date),
//    Member(4, "han", "123458", "han", "han@google.com", date),
//    Member(5, "song", "123459", "song", "song@google.com", date)
//  )
//}

import org.apache.commons.lang.ArrayUtils;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.auth.SVNSSHAuthentication;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.wc.*;


import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.util.EmptyStackException;
import java.util.List;

public class SVNCheckOut {

    public static void main(String[] args) {

        String userName = "tima_e";
        //String userName=System.getProperty("user.name");
        String passWord = "2F10d189";
        String repoUrl = "https://192.168.4.8/svn/" + userName;
        String folder = "/Users/tima/Documents/test";
        File desFolder = new File(folder);
        File[] files = desFolder.listFiles();
        List<File> addedFiles=new ArrayList<File>();
        List<File> modifiedFiles=new ArrayList<File>();
        SVNProperties prop = new SVNProperties();
        Logger logger = LoggerFactory.getLogger(SVNCheckOut.class);
        logger.info("SVNCheckOut");
        files = (File[]) ArrayUtils.removeElement(files, files[1]);


        for (File list : files) {
            System.out.println(list);

        }
        try {
            SVNURL url = SVNURL.parseURIEncoded(repoUrl);
            SVNRepository repo = SVNRepositoryFactory.create(url);
            ISVNOptions options = SVNWCUtil.createDefaultOptions(desFolder,true);
            //write to log file about creating of repo
            logger.info("The repository has beed created");
            //create authenication data
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, passWord);
            repo.setAuthenticationManager(authManager);
            logger.info("Repository ROOT: " + repo.getRepositoryRoot(true));
            logger.info("Repository UUID: " + repo.getRepositoryUUID(true));
            logger.info("Tha latest revision is: " + repo.getLatestRevision());
            //create client manager and set authentication
            SVNClientManager clientManager = SVNClientManager.newInstance();
            clientManager.setAuthenticationManager(authManager);
            logger.info("Auth manager was set");
            //SVNCommitClient commitClient = new SVNCommitClient(authManager,null);
            //commitClient.doCommit(files,false,"by "+userName,prop,null,false,true,SVNDepth.INFINITY);
            //Checking out the folder
            SVNUpdateClient updateClient = clientManager.getUpdateClient();
            updateClient.setIgnoreExternals(false);
            SVNRevision svnRevision = SVNRevision.create(repo.getLatestRevision());
            //long doCheckout=updateClient.doCheckout(url, desFolder, svnRevision, svnRevision, SVNDepth.INFINITY, false);
            //System.out.println(doCheckout);
            logger.info("Checked out the project from repository");

            //Add folder{
            //if (args[0].equals("import")) {
            ISVNEditor editor = repo.getCommitEditor("Test", null, true, null);
            editor.openRoot(-1);
            SVNStatusClient fileStatus = new SVNStatusClient(authManager,options);
            for(File listF:files) {
                SVNStatus svnStatus=fileStatus.doStatus(listF,false);
                //if(svnStatus.getContentsStatus()==SVNStatusType.STATUS_UNVERSIONED)
                if(!svnStatus.isVersioned())
                {
                    editor.addFile(listF.toString(), null, -1);
                    addedFiles.add(listF);
                    logger.info("The " + listF + " file was added");
                }
                else {
                    editor.addFile(listF.toString(), null, -1);
                    modifiedFiles.add(listF);
                    logger.info("The modified" + listF + " file was added");
                }
            }

        //}
            //SVNWCClient svnwcClient = clientManager.getWCClient();
            //svnwcClient.doAdd(desFolder, false, false, false, SVNDepth.INFINITY, false, false, false);


        //logger.info("Updating the repository");
            SVNCommitClient commitClient = clientManager.getCommitClient();
            File[] filesForCom= addedFiles.toArray(new File[addedFiles.size()]);
            File[] filesForComm=modifiedFiles.toArray(new File[modifiedFiles.size()]);
            SVNCommitPacket commitPacket = commitClient.doCollectCommitItems(filesForCom,false,false, SVNDepth.INFINITY,null);

            logger.info(commitPacket.toString());

            //commitClient.doCommit(commitPacket,false,"test");
            logger.info("Lya lya lya");
        //logger.info("New Rebvision "+commitInfo.getNewRevision());

    }
            catch (SVNException e) {
                logger.info("There is an error during creating and exporting repository");
                logger.info(e.getErrorMessage().toString());
                e.printStackTrace();
            } catch (NullPointerException e) {
                logger.info("Empty pointer");
            } catch (EmptyStackException e) {
                e.printStackTrace();
            }


        }

    }


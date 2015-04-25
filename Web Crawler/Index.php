<?php

$to_crawl = "http://youtube.com";

function get_links($url) {
    $input = @file_get_contents($url);
    $regexp = "<a\s[^>]*href=(\"??)([^\" >]*?)\\1[^>]*>(.*)<\/a>";
    preg_match_all("/$regexp/siU", $input, $matches);
    $base_url = parse_url($url, PHP_URL_HOST);
    $l = $matches[2];
    
    foreach($l as $link)
    {
        
        
        if (strpos($link, "#"))
        {
            $link = substr($link, 0, strpos($link,"#"));
        }
        
        if (substr($link,0,1) == ".")
        {
            $link = substr($link,1);
        }
        
        if (substr($link, 0, 7) == "http://")
        {
            $link = substr($link,7);
        }
        else if(substr($link,0,8)=="https://")
        {
            //nothing
        }
        else if (substr($link, 0, 2) == "//")
        {
            $link = substr($link,2);
        }
        else if (substr($link, 0, 1) == "#")
        {
            $link = $url;
        }
        else if (substr($link, 0, 7) == "mailto:")
        {
            $link = "[".$link."]";
        }
        else
        {
            if(substr($link,0,1) != "/")
            {
                $link = $base_url."/".$link;
            }
            else
            {
                $link = $base_url.$link;
            }
        }
        
        echo $link."<br>";
    }
}

get_links($to_crawl);

?>
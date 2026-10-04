/* okur çevrimdışı önbelleği. Yalnızca kendi önbelleğini siler (aynı adresteki diğer uygulamalara dokunmaz). */
const P='okur-',V=P+'50';
const SHELL=['./','index.html','manifest.webmanifest','icon.svg','icon-192.png','icon-512.png','icon-maskable.png'];
self.addEventListener('install',e=>{e.waitUntil(caches.open(V).then(c=>c.addAll(SHELL)).then(()=>self.skipWaiting()))});
self.addEventListener('activate',e=>{e.waitUntil(caches.keys().then(ks=>Promise.all(ks.filter(k=>k.indexOf(P)===0&&k!==V).map(k=>caches.delete(k)))).then(()=>self.clients.claim()))});
self.addEventListener('fetch',e=>{
  const r=e.request;if(r.method!=='GET')return;
  const u=new URL(r.url);
  if(/youtube\.com|ytimg\.com|googlevideo\.com|googleapis\.com/.test(u.hostname))return; /* her zaman ağdan */
  if(r.mode==='navigate'){
    /* Önce ağ (güncelleme gelsin); 3 sn'de yanıt yoksa önbellek: internetsiz açılış takılmaz. Geç gelen yanıt yine önbelleğe yazılır. */
    const net=fetch(r.url,{cache:'no-cache',credentials:'same-origin'}).then(res=>{if(res&&res.ok){const c=res.clone();caches.open(V).then(x=>x.put('index.html',c))}return res});
    e.respondWith(Promise.race([net,new Promise((_,rej)=>setTimeout(rej,3000))]).catch(()=>caches.match('index.html',{cacheName:V}).then(h=>h||net)));
    return;
  }
  e.respondWith(caches.match(r).then(hit=>{
    const net=fetch(r).then(res=>{if(res&&(res.ok||res.type==='opaque')){const c=res.clone();caches.open(V).then(x=>x.put(r,c))}return res}).catch(()=>hit);
    return hit||net;
  }));
});

document.addEventListener('DOMContentLoaded', ()=>{
  const thumbs = Array.from(document.querySelectorAll('.thumb'));
  const lightbox = document.getElementById('lightbox');
  const img = document.getElementById('lightboxImg');
  const closeBtn = document.getElementById('closeBtn');
  const prevBtn = document.getElementById('prev');
  const nextBtn = document.getElementById('next');
  let index = -1;

  function openAt(i){
    const el = thumbs[i];
    if(!el) return;
    const src = el.getAttribute('data-full');
    img.src = src;
    lightbox.setAttribute('aria-hidden','false');
    index = i;
  }

  function close(){
    lightbox.setAttribute('aria-hidden','true');
    img.src = '';
    index = -1;
  }

  thumbs.forEach((t,i)=>{
    t.addEventListener('click', e=>{ e.preventDefault(); openAt(i); });
  });
  closeBtn.addEventListener('click', close);
  lightbox.addEventListener('click', e=>{ if(e.target===lightbox) close(); });
  prevBtn.addEventListener('click', e=>{ e.stopPropagation(); openAt((index-1+thumbs.length)%thumbs.length); });
  nextBtn.addEventListener('click', e=>{ e.stopPropagation(); openAt((index+1)%thumbs.length); });
  document.addEventListener('keydown', e=>{
    if(index===-1) return;
    if(e.key==='Escape') close();
    if(e.key==='ArrowLeft') prevBtn.click();
    if(e.key==='ArrowRight') nextBtn.click();
  });
});

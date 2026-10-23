/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 * AIS Enterprise Systems - Open-Source Supabase Free-Tier Client
 */

import { createClient, SupabaseClient } from '@supabase/supabase-js';

// Default public sandbox credentials (or user-configurable via UI)
const DEFAULT_SUPABASE_URL = 'https://xyzcompany.supabase.co';
const DEFAULT_SUPABASE_ANON_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.fake-anon-key';

let clientInstance: SupabaseClient | null = null;

export function getSupabaseConfig(): { url: string; anonKey: string } {
  try {
    const savedUrl = localStorage.getItem('ais_supabase_url');
    const savedKey = localStorage.getItem('ais_supabase_anon_key');
    if (savedUrl && savedKey) {
      return { url: savedUrl, anonKey: savedKey };
    }
  } catch (e) {}

  return {
    url: DEFAULT_SUPABASE_URL,
    anonKey: DEFAULT_SUPABASE_ANON_KEY
  };
}

export function initSupabase(url?: string, anonKey?: string): SupabaseClient {
  const config = getSupabaseConfig();
  const effectiveUrl = url || config.url;
  const effectiveKey = anonKey || config.anonKey;

  clientInstance = createClient(effectiveUrl, effectiveKey, {
    auth: {
      persistSession: true,
      autoRefreshToken: true
    }
  });

  return clientInstance;
}

export function getSupabase(): SupabaseClient {
  if (!clientInstance) {
    return initSupabase();
  }
  return clientInstance;
}

// Sync transaction to Supabase table
export async function syncTransactionToSupabase(tx: any): Promise<boolean> {
  const client = getSupabase();
  try {
    const { error } = await client
      .from('ais_transactions')
      .upsert({
        id: tx.id,
        client: tx.client,
        type: tx.type,
        amount: tx.amount,
        phone: tx.phone || '',
        ai_score: tx.aiScore || 95,
        ai_note: tx.aiNote || '',
        status: tx.status,
        tenant: tx.tenant || 'ALL',
        updated_at: new Date().toISOString()
      });

    if (error) {
      console.warn('Supabase sync warning:', error.message);
      return false;
    }
    return true;
  } catch (err) {
    console.warn('Supabase offline or unreachable:', err);
    return false;
  }
}

// Fetch all transactions from Supabase
export async function fetchTransactionsFromSupabase(): Promise<any[] | null> {
  const client = getSupabase();
  try {
    const { data, error } = await client
      .from('ais_transactions')
      .select('*')
      .order('id', { ascending: false });

    if (error || !data) return null;
    return data.map((d: any) => ({
      id: d.id,
      client: d.client,
      type: d.type,
      amount: d.amount,
      phone: d.phone,
      aiScore: d.ai_score,
      aiNote: d.ai_note,
      status: d.status,
      tenant: d.tenant
    }));
  } catch (e) {
    return null;
  }
}

// Export global to window for vanilla JS access
if (typeof window !== 'undefined') {
  (window as any).AISSupabase = {
    getSupabase,
    initSupabase,
    getSupabaseConfig,
    syncTransactionToSupabase,
    fetchTransactionsFromSupabase
  };
}

export default {
  getSupabase,
  initSupabase,
  getSupabaseConfig,
  syncTransactionToSupabase,
  fetchTransactionsFromSupabase
};
